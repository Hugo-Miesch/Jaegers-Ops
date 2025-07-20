const express = require('express');
const router = express.Router();
const pool = require('../index'); 

// Route pour récupérer les entrepôts
router.get('/entrepots', async (req, res) => {
    try {
        const conn = await pool.getConnection();
        const [rows] = await conn.query('SELECT id, name, location_lat, location_lng FROM entrepot');
        conn.release();

        // Vérifier si des entrepôts sont trouvés
        if (rows.length === 0) {
            return res.status(404).send('Aucun entrepôt trouvé');
        }

        res.status(200).json(rows); // Renvoie les données au format JSON
    } catch (error) {
        console.error('Erreur lors de la récupération des entrepôts:', error);
        res.status(500).send('Erreur lors de la récupération des entrepôts');
    }
});