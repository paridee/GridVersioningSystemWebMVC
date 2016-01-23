-- phpMyAdmin SQL Dump
-- version 4.4.13.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Creato il: Gen 23, 2016 alle 12:27
-- Versione del server: 5.6.27-0ubuntu1
-- Versione PHP: 5.6.11-1ubuntu3.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gvs`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `Goal`
--

CREATE TABLE IF NOT EXISTS `Goal` (
  `assumption` text,
  `description` text,
  `context` text,
  `id` int(11) NOT NULL,
  `label` text NOT NULL,
  `version` int(11) NOT NULL,
  `measurementGoal` int(11) DEFAULT NULL,
  `state` int(2) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Goal`
--

INSERT INTO `Goal` (`assumption`, `description`, `context`, `id`, `label`, `version`, `measurementGoal`, `state`) VALUES
('pippoassumption', '', '', 32768, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 65536, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 98304, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 131072, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 163840, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 196608, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 229376, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 262144, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 294912, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 327680, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 360448, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 393216, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 425984, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 458752, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 491520, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 524288, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 557056, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 589824, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 622592, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 655360, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 688128, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 720896, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 753664, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 786432, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 819200, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 819201, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 819202, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 851968, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 884736, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 917504, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 950272, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 983040, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1015808, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1048576, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1081344, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1114112, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1146880, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1179648, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1212416, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1245184, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1277952, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1310720, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1343488, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1376256, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1409024, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1441792, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1474560, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1507328, 'stocazzo', 1, NULL, 0),
('pippoassumption', '', '', 1540096, 'stocazzo', 1, NULL, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `GoalToStrategyList`
--

CREATE TABLE IF NOT EXISTS `GoalToStrategyList` (
  `goalID` int(11) NOT NULL,
  `strID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `Grid`
--

CREATE TABLE IF NOT EXISTS `Grid` (
  `id` int(11) NOT NULL,
  `version` int(10) NOT NULL,
  `projID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `GridElementToAuthors`
--

CREATE TABLE IF NOT EXISTS `GridElementToAuthors` (
  `gridElementID` int(11) NOT NULL,
  `author` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `GridToRootGoal`
--

CREATE TABLE IF NOT EXISTS `GridToRootGoal` (
  `gridID` int(11) NOT NULL,
  `goalID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `hibernate_sequences`
--

CREATE TABLE IF NOT EXISTS `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `hibernate_sequences`
--

INSERT INTO `hibernate_sequences` (`sequence_name`, `sequence_next_hi_value`) VALUES
('GridElement', 48);

-- --------------------------------------------------------

--
-- Struttura della tabella `MeasurementGoal`
--

CREATE TABLE IF NOT EXISTS `MeasurementGoal` (
  `id` int(11) NOT NULL,
  `label` text NOT NULL,
  `version` int(11) NOT NULL,
  `description` text,
  `interpretationModel` text,
  `state` int(2) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `MeasurementGoalToQuestion`
--

CREATE TABLE IF NOT EXISTS `MeasurementGoalToQuestion` (
  `goalID` int(11) NOT NULL,
  `quesID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `Metric`
--

CREATE TABLE IF NOT EXISTS `Metric` (
  `count` int(11) DEFAULT NULL,
  `description` text,
  `measurementProcess` text,
  `metricType` text,
  `scaleType` text,
  `label` text NOT NULL,
  `id` int(11) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `state` int(2) NOT NULL DEFAULT '2'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `MetricMeasUnits`
--

CREATE TABLE IF NOT EXISTS `MetricMeasUnits` (
  `metrID` int(11) NOT NULL,
  `measUnit` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `Practitioner`
--

CREATE TABLE IF NOT EXISTS `Practitioner` (
  `id` int(11) NOT NULL,
  `name` text NOT NULL,
  `email` char(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `Project`
--

CREATE TABLE IF NOT EXISTS `Project` (
  `id` int(11) NOT NULL,
  `projectId` char(100) DEFAULT NULL,
  `description` text,
  `creationDate` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `ProjectMeasUnits`
--

CREATE TABLE IF NOT EXISTS `ProjectMeasUnits` (
  `projID` int(11) NOT NULL,
  `measUnit` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `Question`
--

CREATE TABLE IF NOT EXISTS `Question` (
  `id` int(11) NOT NULL,
  `label` text NOT NULL,
  `version` int(11) NOT NULL,
  `question` text,
  `state` int(11) NOT NULL DEFAULT '2'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `QuestionToMetric`
--

CREATE TABLE IF NOT EXISTS `QuestionToMetric` (
  `quesID` int(11) NOT NULL,
  `metrID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `Strategy`
--

CREATE TABLE IF NOT EXISTS `Strategy` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `label` text NOT NULL,
  `description` text,
  `isTerminal` tinyint(1) NOT NULL,
  `strategicProjectId` text,
  `state` int(2) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `StrategyToGoalList`
--

CREATE TABLE IF NOT EXISTS `StrategyToGoalList` (
  `strID` int(11) NOT NULL,
  `goalID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `Goal`
--
ALTER TABLE `Goal`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `Grid`
--
ALTER TABLE `Grid`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `Metric`
--
ALTER TABLE `Metric`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `Practitioner`
--
ALTER TABLE `Practitioner`
  ADD UNIQUE KEY `email` (`email`);

--
-- Indici per le tabelle `Project`
--
ALTER TABLE `Project`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `projectId` (`projectId`);

--
-- Indici per le tabelle `Question`
--
ALTER TABLE `Question`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `Strategy`
--
ALTER TABLE `Strategy`
  ADD PRIMARY KEY (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
