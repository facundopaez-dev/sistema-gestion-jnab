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
const auth = admin.auth();

const USERS_COLLECTION = 'users';

/**
 * Elimina TODOS los usuarios de Firebase Authentication
 */
async function deleteAllAuthUsers(nextPageToken) {
  const result = await auth.listUsers(1000, nextPageToken);

  for (const user of result.users) {
    await auth.deleteUser(user.uid);
    console.log(`Auth eliminado: ${user.email ?? user.uid}`);
  }

  if (result.pageToken) {
    await deleteAllAuthUsers(result.pageToken);
  }
}

/**
 * Elimina TODOS los documentos de la colección users
 */
async function deleteAllFirestoreUsers() {
  const snapshot = await db.collection(USERS_COLLECTION).get();

  if (snapshot.empty) {
    console.log('No hay usuarios en Firestore.');
    return;
  }

  const batch = db.batch();
  snapshot.docs.forEach(doc => {
    batch.delete(doc.ref);
    console.log(`Firestore eliminado: ${doc.id}`);
  });

  await batch.commit();
}

async function main() {
  try {
    console.log('--- Eliminando usuarios de Firebase Auth ---');
    await deleteAllAuthUsers();

    console.log('--- Eliminando usuarios de Firestore ---');
    await deleteAllFirestoreUsers();

    console.log('- Borrado total completado (Auth + Firestore)');
  } catch (error) {
    console.error('- Error durante el borrado total:', error);
  } finally {
    process.exit(0);
  }
}

main();