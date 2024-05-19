import os
import asyncio

from src.schemas.speech2text import Speech2TextAnswer
from src.services.utils.s3 import get_file_from_s3
import torch
from transformers import AutoModelForSpeechSeq2Seq, AutoProcessor, pipeline

device = "cuda:0" if torch.cuda.is_available() else "cpu"
torch_dtype = torch.float16 if torch.cuda.is_available() else torch.float32
model_id = "openai/whisper-large-v3"

model = AutoModelForSpeechSeq2Seq.from_pretrained(
    model_id, torch_dtype=torch_dtype, low_cpu_mem_usage=True, use_safetensors=True
)
model.to(device)

processor = AutoProcessor.from_pretrained(model_id)

pipe = pipeline(
    "automatic-speech-recognition",
    model=model,
    tokenizer=processor.tokenizer,
    feature_extractor=processor.feature_extractor,
    max_new_tokens=128,
    chunk_length_s=30,
    batch_size=16,
    return_timestamps=True,
    torch_dtype=torch_dtype,
    device=device,
)


async def write_file(
    file_bytes: bytes,
    temp_file_path: str
) -> None:
    """
    Writes the given bytes to a file asynchronously.

    Parameters:
    -----------
    file_bytes : bytes
        The bytes to write to the file.
    temp_file_path : str
        The path to the temporary file where the bytes will be written.

    Returns:
    --------
    None
    """
    with open(temp_file_path, "wb") as temp_file:
        temp_file.write(file_bytes)


async def read_file_partition(
    temp_file_path: str,
    start_index: int,
    chunk_size: int
) -> bytes:
    """
    Reads a partition of a file asynchronously.

    Parameters:
    -----------
    temp_file_path : str
        The path to the file.
    start_index : int
        The starting index from where to read the file.
    chunk_size : int
        The size of the chunk to read.

    Returns:
    --------
    bytes
        The data read from the file.
    """
    with open(temp_file_path, "rb") as file:
        file.seek(start_index)
        data = file.read(chunk_size)
    return data


async def model_predict_consistently(
    file_bytes: bytes,
    temp_file_path: str = "temp_file.mp3",
    packet_size_kb: int = 1460
) -> str:
    """
    Predicts consistently using the model.

    Parameters:
    -----------
    file_bytes : bytes
        The bytes representing the input file.
    temp_file_path : str, optional
        The path to the temporary file to be created for processing. Default is "temp_file.mp3".
    packet_size_kb : int, optional
        The size of each packet in kilobytes. Default is 512.

    Returns:
    --------
    str
        The full decoded text.
    """
    await write_file(file_bytes, temp_file_path)

    file_size = os.path.getsize(temp_file_path)
    packet_size_bytes = packet_size_kb * 1024

    decode_text, full_decoded_text = "", ""

    for start_index in range(0, file_size, packet_size_bytes):
        end_index = min(start_index + packet_size_bytes, file_size)
        chunk_size = end_index - start_index

        data = await read_file_partition(temp_file_path, start_index, chunk_size)

        decode_text = pipe(temp_file_path)

        print(decode_text, end="\n\n")
        full_decoded_text = full_decoded_text + decode_text + " "
        await asyncio.sleep(0.5)

    return full_decoded_text


async def speech2text_pipeline(uuid: str) -> Speech2TextAnswer:
    """
    Pipeline for converting speech to text.

    Parameters:
    -----------
    uuid : str
        The unique identifier of the speech file.

    Returns:
    --------
    Speech2TextAnswer
        An object containing the decoded text.
    """
    file_bytes = await get_file_from_s3(uuid)
    decode_text = await model_predict_consistently(file_bytes)

    return Speech2TextAnswer(
        decodedText=decode_text,
    )
