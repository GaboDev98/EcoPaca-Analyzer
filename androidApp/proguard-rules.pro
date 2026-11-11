# Keep source file/line info so Crashlytics stack traces are readable
# (Crashlytics plugin uploads the mapping file for deobfuscation).
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Device/sensor types are stored in Firebase by enum name (DeviceType.valueOf / .name)
-keep enum com.gabodev.iotdevicemonitor.models.** { *; }
