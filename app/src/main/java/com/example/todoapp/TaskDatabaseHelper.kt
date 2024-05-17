import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todoapp.Task

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "todoApp.db"
        private const val DATABASE_VERSION = 3
        private const val TABLE_NAME = "alltasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "description"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create your database table here
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT,$COLUMN_DATE TEXT,$COLUMN_TIME TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade if needed
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertTask(task: Task) {
        if (task.title.isNotBlank() && task.description.isNotBlank() && task.date.isNotBlank() && task.time.isNotBlank()) {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, task.title)
                put(COLUMN_CONTENT, task.description)
                put(COLUMN_DATE, task.date)
                put(COLUMN_TIME, task.time)
            }
            db.insert(TABLE_NAME, null, values)
            db.close() // Close database after use
        }
    }

    fun getAllTasks(): List<Task> {
        val taskList = mutableListOf<Task>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))

            val task = Task(id, title, description, date, time)
            taskList.add(task)
        }
        cursor.close() // Close cursor after use
        db.close() // Close database after use
        return taskList
    }

    fun updateTask(task: Task) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, task.title)
            put(COLUMN_CONTENT, task.description)
            put(COLUMN_DATE, task.date)
            put(COLUMN_TIME, task.time)
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(task.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getTaskById(taskId: Int): Task {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $taskId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
        val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))

        cursor.close()
        db.close()
        return Task(id, title, description, date, time)
    }

    fun deleteTask(taskId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(taskId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}
