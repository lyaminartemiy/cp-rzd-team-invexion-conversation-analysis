from fastapi import APIRouter

from src.schemas.analysis import AnswerSchema
from src.services.controllers.analysis import (
    get_llm_text_analysis,
)


router = APIRouter(prefix="/analyze")


@router.get("/segment/")
async def get_analysis(
    text: str
) -> AnswerSchema:
    """
    Endpoint to retrieve analysis from text.

    Parameters:
    -----------
    text : str
        The text to analyze.

    Returns:
    --------
    AnswerSchema
        The analysis result.
    """
    return await get_llm_text_analysis(text)
