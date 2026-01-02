const admin = require('firebase-admin');
const path = require('path');

const serviceAccountPath = path.resolve(__dirname, '../../serviceAccountKey.json');
const serviceAccount = require(serviceAccountPath);

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();
const auth = admin.auth();

const USERS_COLLECTION = 'users';

const users = [
  {
    email: 'ass@gmail.com',
    password: 'assistant',
    firstName: 'Judith',
    lastName: 'Doe',
    role: 'assistant'
  },
  {
    email: 'org@gmail.com',
    password: 'organizer',
    firstName: 'Jane',
    lastName: 'Doe',
    role: 'organizer'
  },
  {
    email: 'spe@gmail.com',
    password: 'speaker',
    firstName: 'Anya',
    lastName: 'Doe',
    role: 'speaker'
  }
];

async function createOrReplaceUser(user) {
  try {
    let authUser;

    // 1 Verificar si existe en Firebase Auth
    try {
      authUser = await auth.getUserByEmail(user.email);
      console.log(`Usuario Auth ya existe: ${user.email}`);
    } catch {
      // 2 Crear usuario en Auth con password
      authUser = await auth.createUser({
        email: user.email,
        password: user.password
      });
      console.log(`Usuario Auth creado: ${user.email}`);
    }

    const uid = authUser.uid;

    // 3 Guardar datos en Firestore (sin password)
    await db.collection(USERS_COLLECTION).doc(uid).set({
      email: user.email,
      firstName: user.firstName,
      lastName: user.lastName,
      role: user.role
    });

    console.log(`Usuario Firestore guardado: ${user.email}`);
  } catch (error) {
    console.error(`Error con ${user.email}:`, error);
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
