from typing import List, Optional
from pydantic import BaseModel


class ViolatedPhrase(BaseModel):
    """
    Represents a violated phrase.

    Attributes
    ----------
    begin_index : int
        The starting index of the violated phrase.
    end_index : int
        The ending index of the violated phrase.
    """

    beginIndex: int
    endIndex: int
    violatedRegulation: str
    violatedText: str


class AnswerSchema(BaseModel):
    """
    Schema for an answer.

    Attributes
    ----------
    negotiation_text : str
        The text of the negotiation.
    violated_phrases : Optional[List[ViolatedPhrase]]
        List of violated phrases.
    """

    negotiationText: Optional[str]
    violatedPhrases: Optional[List[ViolatedPhrase]]
