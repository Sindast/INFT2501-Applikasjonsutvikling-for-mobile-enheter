package com.example.oving7.managers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlinx.coroutines.selects.select

open class DataBaseManager(context: Context) :
        SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION){

    companion object {

        const val DATABASE_NAME = "MovieDatabase"
        const val DATABASE_VERSION = 1

        const val ID = "_id"

        const val TABLE_MOVIE = "MOVIE"
        const val MOVIE_TITLE = "title"

        const val TABLE_DIRECTOR = "Director"
        const val DIRECTOR_NAME = "name"

        const val TABLE_ACTOR = "Actor"
        const val ACTOR_NAME = "name"

        const val TABLE_ACTOR_MOVIE = "ACTOR_MOVIE"
        const val ACTOR_ID = "actor_id"
        const val MOVIE_ID = "movie_id"

        const val TABLE_DIRECTOR_MOVIE = "DIRECTOR_MOVIE"
        const val DIRECTOR_ID = "director_id"
        //const val MOVIE_ID = "movie_id"

        val JOIN_ACTOR_MOVIE = arrayOf(
            "$TABLE_ACTOR.$ID=$TABLE_ACTOR_MOVIE.$ACTOR_ID",
            "$TABLE_MOVIE.$ID=$TABLE_ACTOR_MOVIE.$MOVIE_ID"
        )
        val JOIN_DIRECTOR_MOVIE = arrayOf(
            "$TABLE_DIRECTOR.$ID=$TABLE_DIRECTOR_MOVIE.$DIRECTOR_ID",
            "$TABLE_MOVIE.$ID=$TABLE_ACTOR_MOVIE.$MOVIE_ID"
        )
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """create table $TABLE_ACTOR (
						$ID integer primary key autoincrement, 
						$ACTOR_NAME text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_MOVIE (
						$ID integer primary key autoincrement, 
						$MOVIE_TITLE text unique not null
						);"""
        )
        db.execSQL(
            """create table $TABLE_DIRECTOR (
						$ID integer primary key autoincrement, 
						$DIRECTOR_NAME text unique not null
						);"""
        )

        db.execSQL(
            """create table $TABLE_ACTOR_MOVIE (
						$ID integer primary key autoincrement, 
						$MOVIE_ID numeric, 
						$ACTOR_ID numeric,
						FOREIGN KEY($ACTOR_ID) REFERENCES $TABLE_ACTOR($ID), 
						FOREIGN KEY($MOVIE_ID) REFERENCES $TABLE_MOVIE($ID)
						);"""
        )
        db.execSQL(
            """create table $TABLE_DIRECTOR_MOVIE (
						$ID integer primary key autoincrement, 
						$MOVIE_ID numeric, 
						$DIRECTOR_ID numeric,
						FOREIGN KEY($DIRECTOR_ID) REFERENCES $TABLE_DIRECTOR($ID), 
						FOREIGN KEY($MOVIE_ID) REFERENCES $TABLE_MOVIE($ID)
						);"""
        )
    }


    override fun onUpgrade(db: SQLiteDatabase, arg1: Int, arg2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTOR_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACTOR")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIRECTOR")
        onCreate(db)
    }

    /**
     *  Drop all information is the database
     */
    fun clear() {
        writableDatabase.use { onUpgrade(it, 0, 0) }
    }
    /**
     * Inserts the movie, director and an actor
     */
    fun insert(movie: String, director: String, actor: String) {
        writableDatabase.use { database ->
            val movieId = insertValueIfNotExists(database, TABLE_MOVIE, MOVIE_TITLE, movie)
            val directorId = insertValueIfNotExists(database, TABLE_DIRECTOR, DIRECTOR_NAME,director)
            val actorId = insertValueIfNotExists(database, TABLE_ACTOR, ACTOR_NAME,actor)

            linkActorAndMovie(database, actorId, movieId)
            linkDirectorAndMovie(database,directorId,movieId)

        }
    }

    /**
     * Pseudo-code for method
     * ```
     *  if (value exists){
     *      return valueId
     *  } else {
     *      insert and return new Id
     *  }
     * ```
     */
    private fun insertValueIfNotExists(
        database: SQLiteDatabase, table: String, field: String, value: String
    ): Long {
        // Query for the value
        query(database, table, arrayOf(ID, field), "$field='$value'").use { cursor ->
            // insert if value doesn't exist
            return if (cursor.count != 0) {
                cursor.moveToFirst()
                cursor.getLong(0) //id of found value
            } else {
                insertValue(database, table, field, value)
            }
        }
    }

    /**
     * Insert the value in given table and field, then return its ID
     */
    private fun insertValue(
        database: SQLiteDatabase, table: String, field: String, value: String
    ): Long {
        val values = ContentValues()
        values.put(field, value.trim())
        return database.insert(table, null, values)
    }

    private fun areValuesNew(
        database: SQLiteDatabase, table: String, field1: String, value1: Long, field2: String, value2: Long
    ): Boolean {
        val c: Cursor = database.rawQuery("SELECT * FROM " +table+" WHERE "+field1+"="+value1+" AND "+field2+" = "+value2,null)
        if(c.moveToFirst())
        {
          Log.e("RAWQUERY_ERROR", "Values already exist in table: $table")
            return false
        }
        else
        {
            return true
        }
    }
    /**
     *  Insert in the *TABLE_ACTOR_MOVIE*, essentially linking an actor and a movie
     */
    private fun linkActorAndMovie(database: SQLiteDatabase, actorId: Long, movieId: Long) {
        val values = ContentValues()
        values.put(ACTOR_ID, actorId)
        values.put(MOVIE_ID, movieId)
        if(areValuesNew(database, TABLE_ACTOR_MOVIE, ACTOR_ID,actorId, MOVIE_ID,movieId)) {
            database.insert(TABLE_ACTOR_MOVIE, null, values)
        }
    }

    private fun linkDirectorAndMovie(database: SQLiteDatabase, directorId: Long, movieId: Long) {
        val values = ContentValues()
        values.put(DIRECTOR_ID, directorId)
        values.put(MOVIE_ID, movieId)
        if(areValuesNew(database, TABLE_DIRECTOR_MOVIE, DIRECTOR_ID,directorId, MOVIE_ID,movieId)) {
            database.insert(TABLE_DIRECTOR_MOVIE, null, values)
        }
    }

    /**
     * Perform a simple query
     *
     * Not the query() function has almost all parameters as *null*, you should check up on these.
     * maybe you don't even need the performRawQuery() function?
     */
    fun performQuery(table: String, columns: Array<String>, selection: String? = null):
            ArrayList<String> {
        assert(columns.isNotEmpty())
        readableDatabase.use { database ->
            query(database, table, columns, selection).use { cursor ->
                return readFromCursor(cursor, columns.size)
            }
        }
    }

    /**
     * Run a raw query, the parameters are for easier debugging and reusable code
     */
    fun performRawQuery(
        select: Array<String>, from: Array<String>, join: Array<String>, where: String? = null
    ): ArrayList<String> {

        val query = StringBuilder("SELECT ")
        for ((i, field) in select.withIndex()) {
            query.append(field)
            if (i != select.lastIndex) query.append(", ")
        }

        query.append(" FROM ")
        for ((i, table) in from.withIndex()) {
            query.append(table)
            if (i != from.lastIndex) query.append(", ")
        }

        query.append(" WHERE ")
        for ((i, link) in join.withIndex()) {
            query.append(link)
            if (i != join.lastIndex) query.append(" and ")
        }

        if (where != null) query.append(" and $where")

        readableDatabase.use { db ->
            db.rawQuery("$query;", null).use { cursor ->
                return readFromCursor(cursor, select.size)
            }
        }
    }


    /**
     * Read the contents from the cursor and return it as an arraylist
     */
    private fun readFromCursor(cursor: Cursor, numberOfColumns: Int): ArrayList<String> {
        val result = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            val item = StringBuilder("")
            for (i in 0 until numberOfColumns) {
                item.append("${cursor.getString(i)} ")
            }
            result.add("$item")
            cursor.moveToNext()
        }
        return result
    }
    /**
     * Use a query with default arguments
     */
    private fun query(
        database: SQLiteDatabase, table: String, columns: Array<String>, selection: String?
    ): Cursor {
        return database.query(table, columns, selection, null, null, null, null, null)
    }
}