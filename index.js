const express = require('express');
const mariadb = require('mariadb');
const bcrypt = require('bcrypt');
const multer = require('multer');
const fs = require('fs');
const app = express();
const port = 3000;

//Android
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));

const pool = mariadb.createPool({
  host: 'localhost',
  user: 'jaegers',
  password: 'jaegers',
  database: 'jaegers_ops',
  connectionLimit: 5
});

pool.getConnection((err, conn) => {
  if (err) {
    console.error('Erreur de connexion à la base de données :', err);
    process.exit(1);
  } else {
    console.log('Connexion à la base de données établie');
    conn.release();
  }
});

const uploadDir = './uploads/';
if (!fs.existsSync(uploadDir)) {
  fs.mkdirSync(uploadDir);
  console.log(`Dossier ${uploadDir} créé`);
} else {
  console.log(`Dossier ${uploadDir} déjà existant`);
}

const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    console.log(`Fichier ${file.originalname} en cours d'upload`);
    cb(null, uploadDir);
  },
  filename: (req, file, cb) => {
    const filename = Date.now() + '-' + file.originalname;
    console.log(`Fichier ${file.originalname} renommé en ${filename}`);
    cb(null, filename);
  }
});

const upload = multer({
  storage: storage,
  limits: { fileSize: 50 * 1024 * 1024 }
}).fields([
  { name: 'name', maxCount: 1 },
  { name: 'email', maxCount: 1 },
  { name: 'password', maxCount: 1 },
  { name: 'image', maxCount: 1 }
]);

async function registerUser(role, email, password, res) {
  try {
    console.log(`Enregistrement d'un nouveau ${role} : ${email}`);
    const hashedPassword = await bcrypt.hash(password, 10);
    const conn = await pool.getConnection();
    await conn.query(
      `INSERT INTO ${role} (email, password) VALUES (?, ?)`,
      [email, hashedPassword]
    );
    conn.release();
    res.status(201).send(`${role} enregistré`);
  } catch (err) {
    console.error(`Erreur lors de l'enregistrement d'un ${role} :`, err);
    res.status(500).send('Erreur lors de l\'enregistrement');
  }
}

async function loginUser(role, email, password, res) {
  try {
    console.log(`Connexion d'un ${role} : ${email}`);
    const conn = await pool.getConnection();
    const rows = await conn.query(`SELECT * FROM ${role} WHERE email = ?`, [email]);
    conn.release();

    if (rows.length === 0) {
      console.log(`Email inconnu : ${email}`);
      return res.status(401).json({ error: 'Email inconnu' }); // Envoi d'une réponse JSON
    }

    const user = rows[0];
    const valid = await bcrypt.compare(password, user.password);
    if (!valid) {
      console.log(`Mot de passe incorrect pour ${email}`);
      return res.status(402).json({ error: 'Mot de passe incorrect' }); // Envoi d'une réponse JSON
    }

    console.log(`Connexion réussie pour ${email}`);
    return res.json({ message: `Bienvenue ${email}` }); // Réponse JSON en cas de succès
  } catch (err) {
    console.error(`Erreur lors de la connexion d'un ${role} :`, err);
    return res.status(500).json({ error: 'Erreur lors de la connexion' }); // Envoi d'une réponse JSON
  }
}


app.get('/entrepots', async (req, res) => {
  console.log('Récupération des entrepôts');
  try {
    const conn = await pool.getConnection();
    const rows = await conn.query('SELECT id, name, location_lat, location_lng FROM entrepot');
    conn.release();
    
    console.log(`Entrepôts récupérés : ${rows.length} entrées`);
    console.log('Données récupérées :', JSON.stringify(rows, null, 2)); // Log les données au format JSON

    if (rows.length === 0) {
      return res.status(404).send('Aucun entrepôt trouvé');
    }

    res.status(200).json(rows); // Renvoie les données au format JSON
  } catch (error) {
    console.error('Erreur lors de la récupération des entrepôts:', error);
    res.status(500).send('Erreur lors de la récupération des entrepôts');
  }
});

app.get('/entrepots/:id', async (req, res) => {
    const { id } = req.params; // Récupérer l'ID de l'entrepôt depuis les paramètres de la requête
    console.log(`Récupération des détails de l'entrepôt avec ID : ${id}`);
    
    try {
        const conn = await pool.getConnection();
        const rows = await conn.query('SELECT * FROM entrepot WHERE id = ?', [id]);
        conn.release();
        console.log(`Détails de l'entrepôt récupérés :`, JSON.stringify(rows, null, 2)); // Log les données au format JSON

        if (rows.length === 0) {
            return res.status(404).send('Entrepôt non trouvé');
        }

        res.status(200).json(rows[0]); // Renvoie les détails de l'entrepôt trouvé
    } catch (error) {
        console.error('Erreur lors de la récupération des détails de l\'entrepôt:', error);
        res.status(500).send('Erreur lors de la récupération des détails de l\'entrepôt');
    }
});

app.get('/entrepots/:id/robots', async (req, res) => {
    const { id } = req.params; // Récupérer l'ID de l'entrepôt depuis les paramètres de la requête
    console.log(`Récupération des robots pour l'entrepôt avec ID : ${id}`);
    
    try {
        const conn = await pool.getConnection();
        const rows = await conn.query('SELECT * FROM robot WHERE entrepot_id = ?', [id]);
        conn.release();
        
        console.log(`Robots récupérés :`, JSON.stringify(rows, null, 2)); // Log les robots au format JSON

        if (rows.length === 0) {
            return res.status(404).send('Aucun robot trouvé pour cet entrepôt');
        }

        res.status(200).json(rows); // Renvoie la liste des robots trouvés
    } catch (error) {
        console.error('Erreur lors de la récupération des robots:', error);
        res.status(500).send('Erreur lors de la récupération des robots');
    }
});

app.get('/robots_details/:id', async (req, res) => {
    const { id } = req.params;
    console.log(`Récupération des détails du robot avec ID : ${id}`);
    
    try {
        const conn = await pool.getConnection();
        const rows = await conn.query('SELECT * FROM robot WHERE id = ?', [id]);
        conn.release();
        
        if (rows.length === 0) {
            return res.status(404).send('Robot non trouvé');
        }

        const robot = rows[0]; 
        res.status(200).json({
            id: robot.id,
            name: robot.name,
            image_path: robot.image_path,
            entrepot_id: robot.entrepot_id
        });
    } catch (error) {
        console.error('Erreur lors de la récupération du robot:', error);
        res.status(500).send('Erreur lors de la récupération des détails du robot');
    }
});

app.post('/client/register', upload, async (req, res) => {
  try {
    console.log('Inscription d\'un client');
    if (!req.body || Object.keys(req.body).length === 0) {
      console.log('Données manquantes');
      return res.status(400).send('Données manquantes');
    }
    const { name, email, password } = req.body;
    console.log(`Nom : ${name}, Email : ${email}`);
    if (!name || !email || !password) {
      console.log('Tous les champs ne sont pas remplis');
      return res.status(400).send('Tous les champs sont requis');
    }

    const files = req.files;
    if (!files || !files['image'] || files['image'].length === 0) {
      console.log('Aucun fichier uploadé');
      return res.status(400).send('Aucun fichier uploadé');
    }

    const conn = await pool.getConnection();

    const emailExist = await conn.query('SELECT * FROM client WHERE email = ?', [email]);
    if (emailExist.length > 0) {
      console.log(`Email déjà existant : ${email}`);
      return res.status(400).send('Email déjà existant');
    }

    const nameExist = await conn.query('SELECT * FROM client WHERE name = ?', [name]);
    if (nameExist.length > 0) {
      console.log(`Nom déjà existant : ${name}`);
      return res.status(400).send('Nom déjà existant');
    }

    const hashedPassword = await bcrypt.hash(password, 10);
    const imagePath = files['image'][0].path;

    await conn.query('INSERT INTO client (name, email, password, image) VALUES (?, ?, ?, ?)', [name, email, hashedPassword, imagePath]);
    conn.release();

    console.log(`Inscription réussie pour ${email}`);
    res.send('Inscription réussie');
  } catch (err) {
    if (err.code === 'ER_DUP_ENTRY') {
      console.log('Doublon détecté :', err);
      res.status(400).send('Email ou nom déjà existant');
    } else if (err instanceof multer.MulterError) {
      console.error('Erreur Multer :', err);
      res.status(500).send('Erreur lors de l\'upload du fichier');
    } else {
      console.error('Erreur lors de l\'inscription :', err);
      res.status(500).send('Erreur lors de l\'inscription');
    }
  }
});

app.post('/client/login', async (req, res) => {
    try {
        const { email, password } = req.body;

        const conn = await pool.getConnection();
        const rows = await conn.query('SELECT * FROM client WHERE email = ?', [email]);
        conn.release();

        if (rows.length === 0) {
            console.log(`Email inconnu : ${email}`);
            return res.status(401).send('Email inconnu');
        }

        const user = rows[0];
        const valid = await bcrypt.compare(password, user.password);
        if (!valid) {
            console.log(`Mot de passe incorrect pour ${email}`);
            return res.status(402).send('Mot de passe incorrect');
        }

        console.log(`Connexion réussie pour ${email}`);
        return res.json({
            id: user.id, 
            message: `Bienvenue ${email}`
        });
    } catch (err) {
        console.error('Erreur lors de la connexion :', err);
        res.status(500).send('Erreur lors de la connexion');
    }
});

app.post('/tickets', async (req, res) => {
    try {
        const { robot_id, entrepot_id, issue } = req.body;
        const conn = await pool.getConnection();
        const priority = 'BASSE';
        const query = 'INSERT INTO ticket (robot_id, entrepot_id, issue, priority, status) VALUES (?, ?, ?, ?, ?)';
        const values = [robot_id, entrepot_id, issue, priority, 'OUVERT'];
        const result = await conn.query(query, values);
        conn.release();
        if (result.affectedRows > 0) {
            res.status(201).json({ id: result.insertId.toString() });
            console.log(`Ticket créé avec l'ID : ${result.insertId.toString()}`);
        } else {
            res.status(500).send('Erreur lors de la création du ticket');
        }
    } catch (error) {
        console.error('Erreur lors de la création du ticket:', error);
        res.status(500).send('Erreur lors de la création du ticket');
    }
});

app.get('/clients/:id/infos', async (req, res) => {
    const { id } = req.params;
    try {
        const conn = await pool.getConnection();
        const rows = await conn.query('SELECT name, image FROM client WHERE id = ?', [id]);
        conn.release();

        if (rows.length === 0) {
            return res.status(404).send('Client non trouvé');
        }

        const client = rows[0];
        res.status(200).json({
            name: client.name,
            image: client.image
        });
    } catch (error) {
        console.error('Erreur lors de la récupération des informations du client:', error);
        res.status(500).send('Erreur lors de la récupération des informations du client');
    }
});

//IOS
app.post('/tech/login', (req, res) => {
  try {
    const { email, password } = req.body;
    console.log("[LOGIN] email reçu :", email, "/ password :", password);
    loginUser('technicien', email, password, res);
  } catch (err) {
    console.error('Erreur lors de la connexion :', err);
    res.status(500).send('Erreur lors de la connexion');
  }
});

app.post('/tech/register', (req, res) => {
  try {
    const { email, password } = req.body;
    registerUser('technicien', email, password, res);
  } catch (err) {
    console.error('Erreur lors de l\'enregistrement :', err);
    res.status(500).send('Erreur lors de l\'enregistrement');
  }
});

app.get('/tech/tickets', async (req, res) => {
  try {
    const conn = await pool.getConnection();
    const query = `
      SELECT t.id, t.issue, t.priority, t.status, t.created_at, t.resolved_at,
             r.name AS jaeger_name, e.name AS entrepot_name
      FROM ticket t
      JOIN robot r ON t.robot_id = r.id
      JOIN entrepot e ON t.entrepot_id = e.id
      ORDER BY t.created_at DESC
    `;
    const rows = await conn.query(query);
    conn.release();
    res.json(rows);
  } catch (error) {
    console.error(error);
    res.status(500).send('Erreur lors de la récupération des tickets');
  }
}); //recuperer les tickets

app.post('/tech/tickets/:id/close', async (req, res) => {
  const { id } = req.params;
  try {
    const conn = await pool.getConnection();
    const result = await conn.query(
      'UPDATE ticket SET status = \'FERME\', resolved_at = CURRENT_TIMESTAMP WHERE id = ?',
      [id]
    );
    conn.release();
    if (result.affectedRows > 0) {
      res.send('Ticket clôturé avec succès');
    } else {
      res.status(404).send('Ticket non trouvé');
    }
  } catch (err) {
    console.error(err);
    res.status(500).send('Erreur lors de la clôture du ticket');
  }
}); //fermer un ticket

app.get('/tech/orders', async (req, res) => {
  try {
    const conn = await pool.getConnection();
    const commandes = await conn.query(`
      SELECT c.id, c.ticket_id, c.technicien_id, c.status, c.created_at,
             t.issue AS ticket_issue, r.name AS jaeger_name
      FROM commande c
      JOIN ticket t ON c.ticket_id = t.id
      JOIN robot r ON t.robot_id = r.id
    `);

    for (const commande of commandes) {
      const pieces = await conn.query(
        'SELECT nom_piece FROM piece_commande WHERE commande_id = ?',
        [commande.id]
      );
      commande.pieces = pieces.map(p => p.nom_piece);
    }

    conn.release();
    res.json(commandes);
  } catch (err) {
    console.error(err);
    res.status(500).send('Erreur lors de la récupération des commandes');
  }
}); //recuperer les commandes

app.post('/tech/orders', async (req, res) => {
  const { ticket_id, technicien_id, pieces } = req.body;
  if (!ticket_id || !technicien_id || !Array.isArray(pieces)) {
    return res.status(400).send('Paramètres manquants ou invalides');
  }
  try {
    const conn = await pool.getConnection();
    const result = await conn.query(
      'INSERT INTO commande (ticket_id, technicien_id, status) VALUES (?, ?, \'EN_COURS\')',
      [ticket_id, technicien_id]
    );
    const commandeId = result.insertId;

    for (const piece of pieces) {
      await conn.query(
        'INSERT INTO piece_commande (commande_id, nom_piece) VALUES (?, ?)',
        [commandeId, piece]
      );
    }

    conn.release();
    res.status(201).json({ commande_id: commandeId });
  } catch (err) {
    console.error(err);
    res.status(500).send("Erreur lors de la création de la commande");
  }
}); //creer une commande

//JAVA
app.post('/super_admin/login', async (req, res) => {
  try {
    const { email, password } = req.body;
    console.log("[LOGIN] email reçu :", email, "/ password :", password);
    loginUser('super_admin', email, password, res);
  } catch (err) {
    console.error('Erreur lors de la connexion :', err);
    res.status(500).send('Erreur lors de la connexion');
  }
});

app.post('/super_admin/register', async (req, res) => {
  try {
    const { email, password } = req.body;
    console.log("[REGISTER] email reçu :", email, "/ password :", password);
    registerUser('super_admin', email, password, res);
  } catch (err) {
    console.error('Erreur lors de l\'enregistrement :', err);
    res.status(500).send('Erreur lors de l\'enregistrement');
  }
});

// Récupérer tous les Clients
app.get('/clients', async (req, res) => {
    try {
        const conn = await pool.getConnection();
        const rows = await conn.query('SELECT id, name, email FROM client');
        conn.release();

        if (rows.length === 0) {
            return res.status(404).send('Aucun client trouvé');
        }

        res.status(200).json(rows);
    } catch (error) {
        console.error('Erreur lors de la récupération des clients:', error);
        res.status(500).send('Erreur lors de la récupération des clients');
    }
});

// Récupérer un Client par ID
app.get('/clients/:id', async (req, res) => {
    const { id } = req.params;
    try {
        const conn = await pool.getConnection();
        const rows = await conn.query('SELECT * FROM client WHERE id = ?', [id]);
        conn.release();

        if (rows.length === 0) {
            return res.status(404).send('Client non trouvé');
        }

        res.status(200).json(rows[0]);
    } catch (error) {
        console.error('Erreur lors de la récupération du client:', error);
        res.status(500).send('Erreur lors de la récupération du client');
    }
});

// Modifier un Client
app.put('/clients/:id', async (req, res) => {
    const { id } = req.params;
    const { name, email, password } = req.body;

    try {
        const conn = await pool.getConnection();
        const updates = [];
        const params = [];

        if (name) {
            updates.push('name = ?');
            params.push(name);
        }
        if (email) {
            updates.push('email = ?');
            params.push(email);
        }
        if (password) {
            const hashedPassword = await bcrypt.hash(password, 10);
            updates.push('password = ?');
            params.push(hashedPassword);
        }

        if (updates.length === 0) {
            return res.status(400).send('Aucun champ à mettre à jour');
        }

        params.push(id);
        await conn.query(`UPDATE client SET ${updates.join(', ')} WHERE id = ?`, params);
        conn.release();
        res.send('Client mis à jour avec succès');
    } catch (error) {
        console.error('Erreur lors de la mise à jour du client:', error);
        res.status(500).send('Erreur lors de la mise à jour du client');
    }
});

// Supprimer un Client
app.delete('/clients/:id', async (req, res) => {
    const { id } = req.params;

    try {
        const conn = await pool.getConnection();
        const result = await conn.query('DELETE FROM client WHERE id = ?', [id]);
        conn.release();

        if (result.affectedRows === 0) {
            return res.status(404).send('Client non trouvé');
        }

        res.send('Client supprimé avec succès');
    } catch (error) {
        console.error('Erreur lors de la suppression du client:', error);
        res.status(500).send('Erreur lors de la suppression du client');
    }
});

// Ajouter un Ticket
app.post('/tickets', async (req, res) => {
    try {
        const { robot_id, entrepot_id, issue } = req.body;
        const conn = await pool.getConnection();
        const priority = 'BASSE';
        const query = 'INSERT INTO ticket (robot_id, entrepot_id, issue, priority, status) VALUES (?, ?, ?, ?, ?)';
        const values = [robot_id, entrepot_id, issue, priority, 'OUVERT'];
        const result = await conn.query(query, values);
        conn.release();
        if (result.affectedRows > 0) {
            res.status(201).json({ id: result.insertId.toString() });
            console.log(`Ticket créé avec l'ID : ${result.insertId}`);
        } else {
            res.status(500).send('Erreur lors de la création du ticket');
        }
    } catch (error) {
        console.error('Erreur lors de la création du ticket:', error);
        res.status(500).send('Erreur lors de la création du ticket');
    }
});

// Récupérer tous les Tickets
app.get('/tickets', async (req, res) => {
    try {
        const conn = await pool.getConnection();
        const query = `
            SELECT t.id, t.issue, t.priority, t.status, t.created_at, 
                   r.name AS robot_name, e.name AS entrepot_name 
            FROM ticket t 
            JOIN robot r ON t.robot_id = r.id 
            JOIN entrepot e ON t.entrepot_id = e.id 
            ORDER BY t.created_at DESC
        `;
        const rows = await conn.query(query);
        conn.release();
        res.json(rows);
    } catch (error) {
        console.error(error);
        res.status(500).send('Erreur lors de la récupération des tickets');
    }
});

// Modifier un Ticket
app.put('/tickets/:id', async (req, res) => {
    const { id } = req.params;
    const { issue, priority, status } = req.body;

    try {
        const conn = await pool.getConnection();
        const updates = [];
        const params = [];

        if (issue) {
            updates.push('issue = ?');
            params.push(issue);
        }
        if (priority) {
            updates.push('priority = ?');
            params.push(priority);
        }
        if (status) {
            updates.push('status = ?');
            params.push(status);
        }

        if (updates.length === 0) {
            return res.status(400).send('Aucun champ à mettre à jour');
        }

        params.push(id);
        await conn.query(`UPDATE ticket SET ${updates.join(', ')} WHERE id = ?`, params);
        conn.release();
        res.send('Ticket mis à jour avec succès');
    } catch (error) {
        console.error('Erreur lors de la mise à jour du ticket:', error);
        res.status(500).send('Erreur lors de la mise à jour du ticket');
    }
});

// Supprimer un Ticket
app.delete('/tickets/:id', async (req, res) => {
    const { id } = req.params;

    try {
        const conn = await pool.getConnection();
        const result = await conn.query('DELETE FROM ticket WHERE id = ?', [id]);
        conn.release();

        if (result.affectedRows === 0) {
            return res.status(404).send('Ticket non trouvé');
        }

        res.send('Ticket supprimé avec succès');
    } catch (error) {
        console.error('Erreur lors de la suppression du ticket:', error);
        res.status(500).send('Erreur lors de la suppression du ticket');
    }
});

// Routes pour les Techniciens
app.post('/techniciens', async (req, res) => {
    const { email, password } = req.body;
    try {
        const conn = await pool.getConnection();
        const hashedPassword = await bcrypt.hash(password, 10);
        await conn.query('INSERT INTO technicien (email, password) VALUES (?, ?)', [email, hashedPassword]);
        conn.release();
        res.status(201).send('Technicien créé avec succès');
    } catch (error) {
        console.error('Erreur lors de la création d\'un technicien:', error);
        res.status(500).send('Erreur lors de la création du technicien');
    }
});

// Supprimer un Technicien
app.delete('/techniciens/:id', async (req, res) => {
    const { id } = req.params;

    try {
        const conn = await pool.getConnection();
        const result = await conn.query('DELETE FROM technicien WHERE id = ?', [id]);
        conn.release();

        if (result.affectedRows === 0) {
            return res.status(404).send('Technicien non trouvé');
        }

        res.send('Technicien supprimé avec succès');
    } catch (error) {
        console.error('Erreur lors de la suppression du technicien:', error);
        res.status(500).send('Erreur lors de la suppression du technicien');
    }
});

// Routes pour les Super Admins
app.post('/super_admins', async (req, res) => {
    const { email, password } = req.body;
    try {
        const conn = await pool.getConnection();
        const hashedPassword = await bcrypt.hash(password, 10);
        await conn.query('INSERT INTO super_admin (email, password) VALUES (?, ?)', [email, hashedPassword]);
        conn.release();
        res.status(201).send('Super Admin créé avec succès');
    } catch (error) {
        console.error('Erreur lors de la création d\'un super admin:', error);
        res.status(500).send('Erreur lors de la création du super admin');
    }
});

// Routes pour les Entrepôts
app.post('/entrepots', async (req, res) => {
    const { name, location_lat, location_lng } = req.body;

    try {
        const conn = await pool.getConnection();
        await conn.query('INSERT INTO entrepot (name, location_lat, location_lng) VALUES (?, ?, ?)', [name, location_lat, location_lng]);
        conn.release();
        res.status(201).send('Entrepôt créé avec succès');
    } catch (error) {
        console.error('Erreur lors de la création d\'un entrepôt:', error);
        res.status(500).send('Erreur lors de la création de l\'entrepôt');
    }
});

// Supprimer un Entrepôt
app.delete('/entrepots/:id', async (req, res) => {
    const { id } = req.params;

    try {
        const conn = await pool.getConnection();
        const result = await conn.query('DELETE FROM entrepot WHERE id = ?', [id]);
        conn.release();

        if (result.affectedRows === 0) {
            return res.status(404).send('Entrepôt non trouvé');
        }

        res.send('Entrepôt supprimé avec succès');
    } catch (error) {
        console.error('Erreur lors de la suppression de l\'entrepôt:', error);
        res.status(500).send('Erreur lors de la suppression de l\'entrepôt');
    }
});

// Routes pour les Robots
app.post('/robots', async (req, res) => {
    const { name, entrepot_id, image_path } = req.body;

    try {
        const conn = await pool.getConnection();
        await conn.query('INSERT INTO robot (name, entrepot_id, image_path) VALUES (?, ?, ?)', [name, entrepot_id, image_path]);
        conn.release();
        res.status(201).send('Robot créé avec succès');
    } catch (error) {
        console.error('Erreur lors de la création d\'un robot:', error);
        res.status(500).send('Erreur lors de la création du robot');
    }
});

// Supprimer un Robot
app.delete('/robots/:id', async (req, res) => {
    const { id } = req.params;

    try {
        const conn = await pool.getConnection();
        const result = await conn.query('DELETE FROM robot WHERE id = ?', [id]);
        conn.release();

        if (result.affectedRows === 0) {
            return res.status(404).send('Robot non trouvé');
        }

        res.send('Robot supprimé avec succès');
    } catch (error) {
        console.error('Erreur lors de la suppression du robot:', error);
        res.status(500).send('Erreur lors de la suppression du robot');
    }
});

// Routes pour les Commandes
app.post('/commandes', async (req, res) => {
    const { ticket_id, technicien_id } = req.body;

    try {
        const conn = await pool.getConnection();
        const result = await conn.query('INSERT INTO commande (ticket_id, technicien_id, status) VALUES (?, ?, ?)', [ticket_id, technicien_id, 'EN_COURS']);
        conn.release();
        res.status(201).json({ id: result.insertId });
    } catch (error) {
        console.error('Erreur lors de la création de la commande:', error);
        res.status(500).send('Erreur lors de la création de la commande');
    }
});

// Supprimer une Commande
app.delete('/commandes/:id', async (req, res) => {
    const { id } = req.params;

    try {
        const conn = await pool.getConnection();
        const result = await conn.query('DELETE FROM commande WHERE id = ?', [id]);
        conn.release();

        if (result.affectedRows === 0) {
            return res.status(404).send('Commande non trouvée');
        }

        res.send('Commande supprimée avec succès');
    } catch (error) {
        console.error('Erreur lors de la suppression de la commande:', error);
        res.status(500).send('Erreur lors de la suppression de la commande');
    }
});



app.listen(port, '0.0.0.0', () => {
  console.log(`API en écoute sur http://localhost:${port}`);
});
