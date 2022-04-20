package com.example.discgolfapp_v1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

@Database(entities = [(Disc::class)], version = 1, exportSchema = false)
abstract class DiscDatabase: RoomDatabase()
{
    abstract fun discDao(): DiscDao

    companion object
    {
        @Volatile
        private var INSTANCE: DiscDatabase? = null
        fun getDatabase(context: Context): DiscDatabase
        {
            val tempInstance = INSTANCE
            if ( tempInstance != null )
            {
                return tempInstance
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiscDatabase::class.java,
                    "disc_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
