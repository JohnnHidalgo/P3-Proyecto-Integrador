# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Sesion 27: reglas propias para el build de release (isMinifyEnabled = true).
#
# Gson (usado por el convertidor de Retrofit, ver data/remote/RetrofitInstance.kt)
# deserializa TareaDto usando reflexion, leyendo sus campos por nombre. Sin
# esta regla, R8 podria renombrar u ofuscar esos campos y romper el parseo
# del JSON que llega de la API de ejemplo.
-keep class com.upb.taskmanager.data.remote.TareaDto { *; }

# Room genera sus propias reglas de "consumer proguard" dentro de su libreria
# y las aplica automaticamente; no hace falta duplicarlas aqui.
