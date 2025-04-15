const express = require('express');
const mariadb = require('mariadb');
const bcrypt = require('bcrypt');
const app = express();
const port = 3000;

app.use(express.json());

const pool = mariadb.createPool({
  host: 'localhost',
  user: 'root',
  password: 'ton_mot_de_passe',
  database: 'jaegers_ops',
  connectionLimit: 5
});

// 🔹 Fonction d'inscription
async function registerUser(role, email, password, res) {
  try {
    const hashedPassword = await bcrypt.hash(password, 10);
    const conn = await pool.getConnection();
    await conn.query(
      `INSERT INTO ${role} (email, password) VALUES (?, ?)`,
      [email, hashedPassword]
    );
    conn.release();
    res.status(201).send(`${role} inscrit avec succès`);
  } catch (err) {
    console.error(err);
    res.status(500).send('Erreur lors de l\'inscription');
  }
}

// 🔹 Fonction de connexion
async function loginUser(role, email, password, res) {
  try {
    const conn = await pool.getConnection();
    const rows = await conn.query(`SELECT * FROM ${role} WHERE email = ?`, [email]);
    conn.release();

    if (rows.length === 0) return res.status(401).send('Email inconnu');

    const user = rows[0];
    const valid = await bcrypt.compare(password, user.password);
    if (!valid) return res.status(401).send('Mot de passe incorrect');

    res.send(`Bienvenue ${role} : ${email}`);
  } catch (err) {
    console.error(err);
    res.status(500).send('Erreur lors de la connexion');
  }
}

// ✅ ROUTES CLIENT
app.post('/client/register', (req, res) => {
  const { email, password } = req.body;
  registerUser('client', email, password, res);
});

app.post('/client/login', (req, res) => {
  const { email, password } = req.body;
  loginUser('client', email, password, res);
});

// ✅ ROUTES TECHNICIEN
app.post('/tech/register', (req, res) => {
  const { email, password } = req.body;
  registerUser('technicien', email, password, res);
});

app.post('/tech/login', (req, res) => {
  const { email, password } = req.body;
  loginUser('technicien', email, password, res);
});

app.listen(port, () => {
  console.log(`API en écoute sur http://localhost:${port}`);
});
