#===============================================================================
# PROGUARD
# Configuration for code shrinking, obfuscation and optimization.
#===============================================================================

#===============================================================================
# GENERAL
#===============================================================================
-injars target/botnet-1.0-jar-with-dependencies.jar
-outjars target/botnet-1.0-optimized.jar
-libraryjars <java.home>/lib/rt.jar
-target 1.8
-dontwarn
-ignorewarnings
-forceprocessing

-printmapping data/proguard/botnet.map

-optimizationpasses 3

#===============================================================================
# APPLICATION
#===============================================================================

-keeppackagenames com.acmutv.botnet

-keep class com.acmutv.botnet.BotMain {
    public static void main(java.lang.String[]);
}

#===============================================================================
# JAVA
#===============================================================================

-adaptresourcefilenames **.properties,**.xml,**.yaml,**.yml,**json

-adaptresourcefilecontents **.properties,**.xml,**.yaml,**.yml,**json,META-INF/MANIFEST.MF

-renamesourcefileattribute SourceFile 

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod 

-keepclasseswithmembernames,includedescriptorclasses class * { 
    native <methods>; 
} 

-keepclassmembers,allowoptimization enum * { 
    public static **[] values(); 
    public static ** valueOf(java.lang.String); 
} 

-keepclassmembers class * implements java.io.Serializable { 
    static final long serialVersionUID; 
    private static final java.io.ObjectStreamField[] serialPersistentFields; 
    private void writeObject(java.io.ObjectOutputStream); 
    private void readObject(java.io.ObjectInputStream); 
    java.lang.Object writeReplace(); 
    java.lang.Object readResolve(); 
}

#===============================================================================
# LIBRARIES
#===============================================================================

-keep,includedescriptorclasses class org.apache.logging.log4j.core.** {
	*;
}

-keep,includedescriptorclasses class com.fasterxml.** {
	*;
}

-keep,includedescriptorclasses class org.** {
	*;
}

