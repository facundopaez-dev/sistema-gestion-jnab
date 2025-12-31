const admin = require("firebase-admin");

const serviceAccount = require("../../serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();
const collection = db.collection("symposiums");

async function deleteSymposiums() {
  try {
    const snapshot = await collection.get();
    const batch = db.batch();

    snapshot.docs.forEach(doc => {
      batch.delete(doc.ref);
    });

    await batch.commit();
    console.log("✅ Todos los simposios fueron borrados.");
  } catch (error) {
    console.error("❌ Error borrando simposios:", error);
  }
}

deleteSymposiums();