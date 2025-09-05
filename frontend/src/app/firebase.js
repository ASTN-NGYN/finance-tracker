import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyDMqf1d5OBF6sVmqa5qsZ9T7SFW0Ld9XBg",
  authDomain: "finance-tracker-2509b.firebaseapp.com",
  projectId: "finance-tracker-2509b",
  storageBucket: "finance-tracker-2509b.firebasestorage.app",
  messagingSenderId: "276456416364",
  appId: "1:276456416364:web:28c604eb71bc8b6f8ba449",
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const googleProvider = new GoogleAuthProvider();
export default app;




/**
 *   apiKey: "AIzaSyDMqf1d5OBF6sVmqa5qsZ9T7SFW0Ld9XBg",
  authDomain: "finance-tracker-2509b.firebaseapp.com",
  projectId: "finance-tracker-2509b",
  storageBucket: "finance-tracker-2509b.firebasestorage.app",
  messagingSenderId: "276456416364",
  appId: "1:276456416364:web:28c604eb71bc8b6f8ba449",
  measurementId: "G-9C28XN643V"
 */