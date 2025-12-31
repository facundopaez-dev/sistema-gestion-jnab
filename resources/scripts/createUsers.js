// importar Firebase Admin SDK y path
const admin = require('firebase-admin');
const path = require('path');

// resolver la ruta del serviceAccountKey.json
const serviceAccountPath = path.resolve(__dirname, '../../serviceAccountKey.json');
const serviceAccount = require(serviceAccountPath);

// inicializar la app de Firebase con la clave
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

// referencia a Firestore
const db = admin.firestore();

// lista de usuarios a crear
const users = [
  {
    email: 'ass@gmail.com',
    firstName: 'Judith',
    lastName: 'Doe',
    role: 'assistant'
  },
  {
    email: 'org@gmail.com',
    firstName: 'Jane',
    lastName: 'Doe',
    role: 'organizer'
  },
  {
    email: 'spe@gmail.com',
    firstName: 'Anya',
    lastName: 'Doe',
    role: 'speaker'
  }
];

// nombre de la colección donde se guardan los usuarios
const USERS_COLLECTION = 'users';

async function createOrReplaceUser(user) {
  try {
    // buscar si el usuario ya existe por email
    const querySnapshot = await db.collection(USERS_COLLECTION)
      .where('email', '==', user.email)
      .get();

    // si existe, eliminar
    if (!querySnapshot.empty) {
      querySnapshot.forEach(doc => {
        console.log(`Eliminando usuario existente: ${doc.id}`);
        doc.ref.delete();
      });
    }

    // crear el usuario
    const docRef = db.collection(USERS_COLLECTION).doc(); // doc() genera un id automático
    await docRef.set(user);
    console.log(`Usuario creado: ${user.email}`);
  } catch (error) {
    console.error(`Error con el usuario ${user.email}:`, error);
  }
}

async function main() {
  for (const user of users) {
    await createOrReplaceUser(user);
  }
  console.log('Todos los usuarios han sido procesados.');
  process.exit(0);
}

main();