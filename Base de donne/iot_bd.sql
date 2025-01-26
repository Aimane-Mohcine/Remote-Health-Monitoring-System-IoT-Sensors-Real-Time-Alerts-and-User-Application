-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : dim. 26 jan. 2025 à 21:52
-- Version du serveur : 10.4.27-MariaDB
-- Version de PHP : 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `iot_bd`
--

-- --------------------------------------------------------

--
-- Structure de la table `anomalies`
--

CREATE TABLE `anomalies` (
  `id` bigint(20) NOT NULL,
  `sensor_id` varchar(255) DEFAULT NULL,
  `anomaly_type` varchar(255) DEFAULT NULL,
  `commentaire` varchar(255) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `heart_rate` double DEFAULT NULL,
  `heure` time(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `anomalies`
--

INSERT INTO `anomalies` (`id`, `sensor_id`, `anomaly_type`, `commentaire`, `date`, `heart_rate`, `heure`) VALUES
(1, 'HEART_SENSOR_001', 'Fréquence critique élevée', ' La fréquence cardiaque est extrêmement élevée, ce qui peut indiquer une urgence médicale.', '2025-01-26', 232.2, '13:35:55.000000'),
(2, 'HEART_SENSOR_001', 'Données incohérentes', 'Les données reçues semblent incorrectes. Cela peut être dû à un problème technique ou à un mauvais positionnement du capteur.', '2025-01-26', 365.2, '13:37:01.000000'),
(3, 'HEART_SENSOR_001', 'Fréquence critique élevée', ' La fréquence cardiaque est extrêmement élevée, ce qui peut indiquer une urgence médicale.', '2025-01-26', 254.8, '13:38:07.000000'),
(4, 'HEART_SENSOR_001', 'Fréquence critique élevée', ' La fréquence cardiaque est extrêmement élevée, ce qui peut indiquer une urgence médicale.', '2025-01-26', 267.2, '13:39:14.000000'),
(5, 'HEART_SENSOR_001', 'Données incohérentes', 'Les données reçues semblent incorrectes. Cela peut être dû à un problème technique ou à un mauvais positionnement du capteur.', '2025-01-26', 296.8, '13:40:20.000000'),
(6, 'HEART_SENSOR_001', 'Fréquence critique élevée', ' La fréquence cardiaque est extrêmement élevée, ce qui peut indiquer une urgence médicale.', '2025-01-26', 284.8, '13:41:26.000000'),
(7, 'HEART_SENSOR_001', 'Fréquence critique élevée', ' La fréquence cardiaque est extrêmement élevée, ce qui peut indiquer une urgence médicale.', '2025-01-26', 265.6, '13:42:33.000000');

-- --------------------------------------------------------

--
-- Structure de la table `patient`
--

CREATE TABLE `patient` (
  `id` bigint(20) NOT NULL,
  `adresse` varchar(255) DEFAULT NULL,
  `cloudinary_public_id` longtext DEFAULT NULL,
  `date_naissance` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mot_de_passe` varchar(255) DEFAULT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `photo_url` longtext DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL,
  `sensor_id` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) DEFAULT NULL,
  `weight` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `patient`
--

INSERT INTO `patient` (`id`, `adresse`, `cloudinary_public_id`, `date_naissance`, `email`, `mot_de_passe`, `nom`, `photo_url`, `prenom`, `sensor_id`, `telephone`, `weight`) VALUES
(9, 'Rabat agdal', 'gs7w43dxddbsgysg1yk6', '2002-10-17', 'aimanemohcine2001@gmail.com', '0606f386780a39475146f1795a7d02261d4324c440cf887d7f40474d550c7ead', 'Boukries', 'http://res.cloudinary.com/dwkncy4ed/image/upload/v1737888477/gs7w43dxddbsgysg1yk6.jpg', 'badr', 'HEART_SENSOR_001', '0666437578', 100);

-- --------------------------------------------------------

--
-- Structure de la table `statistiques`
--

CREATE TABLE `statistiques` (
  `sensor_id` varchar(255) NOT NULL,
  `date_naissance` varchar(255) DEFAULT NULL,
  `last_update` datetime(6) DEFAULT NULL,
  `max` double DEFAULT NULL,
  `min` double DEFAULT NULL,
  `moyenne` double DEFAULT NULL,
  `nombre_anomalie` bigint(20) DEFAULT NULL,
  `weight` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `statistiques`
--

INSERT INTO `statistiques` (`sensor_id`, `date_naissance`, `last_update`, `max`, `min`, `moyenne`, `nombre_anomalie`, `weight`) VALUES
('HEART_SENSOR_001', '2002-10-17', '2025-01-26 20:52:00.000000', 81.8, 72, 79.06, 0, 283.5);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `anomalies`
--
ALTER TABLE `anomalies`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `patient`
--
ALTER TABLE `patient`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKbawli8xm92f30ei6x9p3h8eju` (`email`),
  ADD UNIQUE KEY `UKqascwsx0wmcmftu0qqvow9g6n` (`sensor_id`);

--
-- Index pour la table `statistiques`
--
ALTER TABLE `statistiques`
  ADD PRIMARY KEY (`sensor_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `anomalies`
--
ALTER TABLE `anomalies`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `patient`
--
ALTER TABLE `patient`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
