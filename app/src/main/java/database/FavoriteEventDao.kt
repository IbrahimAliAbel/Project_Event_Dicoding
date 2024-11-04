package database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>

    @Delete
    suspend fun delete(event: FavoriteEvent)
}