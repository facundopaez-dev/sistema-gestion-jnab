const admin = require("firebase-admin");
const fs = require("fs");

const serviceAccount = require("../../serviceAccountKey.json");
const symposiums = JSON.parse(fs.readFileSync("symposiums.json", "utf8"));

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
}

uploadSymposiums();