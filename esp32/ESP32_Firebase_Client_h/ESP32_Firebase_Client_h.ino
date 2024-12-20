#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include "time.h"

// Otros includes y definiciones...
#include "addons/TokenHelper.h"

// Credenciales de Wi-Fi y Firebase
#define WIFI_SSID "DALLAS"
#define WIFI_PASSWORD "41774284"
#define API_KEY "AIzaSyDe0Zr72jDwPl3Kdv53gTwoRblbfvQY_oo"
#define USER_EMAIL "developer.gabo@gmail.com"
#define USER_PASSWORD "12345678"
#define DATABASE_URL "https://ecopacaanalyzer-default-rtdb.firebaseio.com/"

// Definir objetos de Firebase
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

// Variables de la base de datos
String uniqueDeviceId;
String databasePath;
String tempPath = "/temperature";
String humPath = "/humidity";
String presPath = "/pressure";
String timePath = "/timestamp";

// Nodo principal (para ser actualizado en cada ciclo)
String parentPath;

int timestamp;
FirebaseJson json;

const char* ntpServer = "pool.ntp.org";

// Variables de temporizador (enviar nuevas lecturas cada 3 minutos)
unsigned long sendDataPrevMillis = 0;
unsigned long timerDelay = 180000; // 3 minutos = 180000 milisegundos

// Función para generar un ID único a partir de la dirección MAC
String generateUniqueId() {
  uint64_t chipId = ESP.getEfuseMac();  // Obtener la dirección MAC única (usada como ID)
  String uniqueId = String(chipId, HEX); // Convertir la dirección MAC a string (en HEX)
  return uniqueId;
}

// Inicializar Wi-Fi
void initWiFi() {
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Conectando a WiFi ..");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(1000);
  }
  Serial.println(WiFi.localIP());
  Serial.println();
}

// Función que obtiene el tiempo actual en formato epoch
unsigned long getTime() {
  time_t now;
  struct tm timeinfo;
  if (!getLocalTime(&timeinfo)) {
    return 0;
  }
  time(&now);
  return now;
}

void setup() {
  Serial.begin(115200);

  // Inicializar Wi-Fi
  initWiFi();

  // Configirar reloj
  configTime(0, 0, ntpServer);

  // Asignar la clave API (requerida)
  config.api_key = API_KEY;

  // Asignar las credenciales de inicio de sesión del usuario
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;

  // Asignar la URL de la base de datos (requerida)
  config.database_url = DATABASE_URL;

  Firebase.reconnectWiFi(true);
  fbdo.setResponseSize(4096);

  // Asignar la función de callback para la tarea de generación de token
  config.token_status_callback = tokenStatusCallback; // ver addons/TokenHelper.h

  // Asignar el número máximo de intentos para la generación de tokens
  config.max_token_generation_retry = 5;

  // Inicializar la biblioteca con la autenticación y configuración de Firebase
  Firebase.begin(&config, &auth);

  // Usar el ID único para Firebase
  uniqueDeviceId = generateUniqueId();
  databasePath = "/DevicesData/" + uniqueDeviceId + "/readings";
}

void loop() {
  // Enviar nuevas lecturas a la base de datos
  if (Firebase.ready() && (millis() - sendDataPrevMillis > timerDelay || sendDataPrevMillis == 0)) {
    sendDataPrevMillis = millis();

    // Obtener la marca de tiempo actual
    timestamp = getTime();
    Serial.print("Tiempo: ");
    Serial.println(timestamp);

    parentPath = databasePath + "/" + String(timestamp);

    // Generar datos aleatorios para simular lecturas de sensores
    json.set(tempPath.c_str(), String(random(20, 30)));  // Temperatura simulada entre 20-30 °C
    json.set(humPath.c_str(), String(random(40, 60)));   // Humedad simulada entre 40-60%
    json.set(presPath.c_str(), String(random(1000, 1020)));  // Presión simulada entre 1000 y 1020 hPa
    json.set(timePath, String(timestamp));

    // Enviar los datos a Firebase
    Serial.printf("Configurar json... %s\n", Firebase.RTDB.setJSON(&fbdo, parentPath.c_str(), &json) ? "ok" : fbdo.errorReason().c_str());
  }
}