const admin = require('firebase-admin');
const path = require('path');

// ruta absoluta al serviceAccountKey.json
const serviceAccountPath = path.resolve(__dirname, '../../serviceAccountKey.json');
const serviceAccount = require(serviceAccountPath);

// inicializar Firebase
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// nombre de la colección de usuarios
const USERS_COLLECTION = 'users';

async function deleteAllUsers() {
  try {
    const snapshot = await db.collection(USERS_COLLECTION).get();

    if (snapshot.empty) {
      console.log('No hay usuarios para eliminar.');
      return;
    }

    // eliminar cada documento
    const batch = db.batch();
    snapshot.docs.forEach(doc => {
      batch.delete(doc.ref);
      console.log(`Eliminando usuario: ${doc.id}`);
    });

    await batch.commit();
    console.log('Todos los usuarios han sido eliminados.');
  } catch (error) {
    console.error('Error al eliminar usuarios:', error);
  }
}

deleteAllUsers().then(() => process.exit(0));