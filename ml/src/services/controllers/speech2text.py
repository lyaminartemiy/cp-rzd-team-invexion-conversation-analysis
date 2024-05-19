from src.schemas.speech2text import Speech2TextAnswer
from src.services.utils.speech2text import (
    speech2text_pipeline,
)


async def get_speech2text_pipeline(uuid: str) -> Speech2TextAnswer:
    """
    Retrieves the speech-to-text pipeline result for a given UUID.

    Parameters:
    -----------
    uuid : str
        The unique identifier of the speech file.

    Returns:
    --------
    Speech2TextAnswer
        An object containing the decoded text.
    """
    return await speech2text_pipeline(uuid)
