const admin = require("firebase-admin");
const fs = require("fs");
const path = require("path");

// service account (raíz del proyecto)
const serviceAccountPath = path.resolve(__dirname, "../../serviceAccountKey.json");
const serviceAccount = require(serviceAccountPath);

// archivo JSON de symposiums
const symposiumsPath = path.resolve(__dirname, "../data/symposiums.json");
const symposiums = JSON.parse(fs.readFileSync(symposiumsPath, "utf8"));

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();
const collection = db.collection("symposiums");

async function uploadSymposiums() {
  for (const symposium of symposiums) {
    try {
      await collection.add(symposium);
      console.log(`✔️ Subido: ${symposium.title}`);
    } catch (error) {
      console.error(`❌ Error al subir ${symposium.title}:`, error);
    }
  }
  
  console.log("✅ Carga completada.");
  process.exit(0);
}

uploadSymposiums();