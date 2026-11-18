# Room database
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Keep models for JSON serialization/deserialization
-keepclassmembers class com.nazeeran.quillvibe.notes.data.** { *; }
-keep class com.nazeeran.quillvibe.notes.data.** { *; }
