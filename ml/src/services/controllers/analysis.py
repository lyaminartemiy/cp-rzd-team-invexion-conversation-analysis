from src.schemas.analysis import AnswerSchema
from src.services.utils.analysis import (
    generate_answer_from_text,
)


async def get_llm_text_analysis(text: str) -> AnswerSchema:
    """
    Get analysis from text.

    Segments violations from text and returns the analysis.

    Parameters
    ----------
    text : str
        The text to analyze.

    Returns
    -------
    AnswerSchema
        Analysis result.
    """
    return await generate_answer_from_text(text)
