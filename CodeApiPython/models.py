from pydantic import BaseModel
from typing import List, Optional

class GameMeta(BaseModel):
    game_id: int
    author: Optional[str] = None
    publisher: Optional[str] = None
    category: Optional[str] = None

class GameWithRating(GameMeta):
    rating: float
    from_wishlist: Optional[bool] = False

class UserData(BaseModel):
    user_id: int
    age: Optional[int] = None
    purchases: List[GameWithRating]
    available_games: List[GameMeta]
