from pydantic import BaseModel


class Speech2TextAnswer(BaseModel):
    """
    Schema for the speech-to-text answer.

    Attributes:
    -----------
    decodedText : str
        The decoded text from the speech.
    """
    decodedText: str
