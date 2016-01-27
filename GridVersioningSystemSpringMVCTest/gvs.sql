-- phpMyAdmin SQL Dump
-- version 4.4.13.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Creato il: Gen 26, 2016 alle 17:02
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
('assumption1', 'goal1', 'context1', 1, 'g1', 1, 2, 0),
('assumption2', 'goal2', 'context2', 9, 'g2', 1, 10, 0),
('assumption1', 'goal1', 'context1', 21, 'g1', 2, 15, 0),
('assumption1', 'goal1', 'context1', 23, 'g1', 3, 22, 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `GoalToStrategyList`
--

CREATE TABLE IF NOT EXISTS `GoalToStrategyList` (
  `goalID` int(11) NOT NULL,
  `strID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `GoalToStrategyList`
--

INSERT INTO `GoalToStrategyList` (`goalID`, `strID`) VALUES
(1, 8),
(9, 13),
(9, 14),
(21, 8),
(23, 8);

-- --------------------------------------------------------

--
-- Struttura della tabella `Grid`
--

CREATE TABLE IF NOT EXISTS `Grid` (
  `id` int(11) NOT NULL,
  `version` int(10) NOT NULL,
  `projID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Grid`
--

INSERT INTO `Grid` (`id`, `version`, `projID`) VALUES
(1, 1, 1),
(3, 2, 1),
(4, 3, 1);

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

--
-- Dump dei dati per la tabella `GridToRootGoal`
--

INSERT INTO `GridToRootGoal` (`gridID`, `goalID`) VALUES
(1, 1),
(1, 9),
(3, 21),
(3, 9),
(4, 23),
(4, 9);

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
('Grid', 1),
('Project', 1),
('GridElement', 1);

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

--
-- Dump dei dati per la tabella `MeasurementGoal`
--

INSERT INTO `MeasurementGoal` (`id`, `label`, `version`, `description`, `interpretationModel`, `state`) VALUES
(2, 'mg1', 1, 'meas goal 1', 'interpr1', 0),
(10, 'mg2', 1, 'meas goal 2', 'interpr2', 0),
(15, 'mg1', 2, 'meas goal 1', 'interpr1 pippo stpocazz', 0),
(22, 'mg1', 2, 'meas goal 1', 'interpr1 pippo stpocazz', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `MeasurementGoalToQuestion`
--

CREATE TABLE IF NOT EXISTS `MeasurementGoalToQuestion` (
  `goalID` int(11) NOT NULL,
  `quesID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `MeasurementGoalToQuestion`
--

INSERT INTO `MeasurementGoalToQuestion` (`goalID`, `quesID`) VALUES
(2, 3),
(2, 5),
(10, 11),
(15, 16),
(15, 18),
(22, 16),
(22, 18);

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

--
-- Dump dei dati per la tabella `Metric`
--

INSERT INTO `Metric` (`count`, `description`, `measurementProcess`, `metricType`, `scaleType`, `label`, `id`, `version`, `state`) VALUES
(0, 'm3 descr', 'm3 mProcess', 'BASE', 'm3 stype', 'm3', 4, 1, 0),
(0, 'm2 descr', 'm2 mProcess', 'BASE', 'm2 stype', 'm2', 6, 1, 0),
(0, 'm1 descr', 'm1 mProcess', 'BASE', 'm1 stype', 'm1', 7, 1, 0),
(0, 'm4 descr', 'm4 mProcess', 'BASE', 'm4 stype', 'm4', 12, 1, 0),
(0, 'm3 descr', 'm3 mProcess', 'BASE', 'm3 stype', 'm3', 17, 1, 0),
(0, 'm2 descr', 'm2 mProcess', 'BASE', 'm2 stype', 'm2', 19, 1, 0),
(0, 'm1 descr', 'm1 mProcess', 'BASE', 'm1 stype', 'm1', 20, 1, 0);

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

--
-- Dump dei dati per la tabella `Project`
--

INSERT INTO `Project` (`id`, `projectId`, `description`, `creationDate`) VALUES
(1, 'progetto di prova', 'descrizione progetto', '18/02/2015 11:51');

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

--
-- Dump dei dati per la tabella `Question`
--

INSERT INTO `Question` (`id`, `label`, `version`, `question`, `state`) VALUES
(3, 'q2', 1, 'question 2', 0),
(5, 'q1', 1, 'question 1', 0),
(11, 'q3', 1, 'question 3', 0),
(16, 'q2', 1, 'question 2', 0),
(18, 'q1', 1, 'question 1', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `QuestionToMetric`
--

CREATE TABLE IF NOT EXISTS `QuestionToMetric` (
  `quesID` int(11) NOT NULL,
  `metrID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `QuestionToMetric`
--

INSERT INTO `QuestionToMetric` (`quesID`, `metrID`) VALUES
(3, 4),
(5, 6),
(5, 7),
(11, 12),
(16, 17),
(18, 19),
(18, 20);

-- --------------------------------------------------------

--
-- Struttura della tabella `Strategy`
--

CREATE TABLE IF NOT EXISTS `Strategy` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `label` text NOT NULL,
  `description` text,
  `strategyType` char(20) NOT NULL DEFAULT 'TERMINAL',
  `strategicProjectId` text,
  `state` int(2) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Strategy`
--

INSERT INTO `Strategy` (`id`, `version`, `label`, `description`, `strategyType`, `strategicProjectId`, `state`) VALUES
(8, 1, 's1', 'strat1', 'NONTERMINAL', '0', 0),
(13, 1, 's3', 'strat3', 'TERMINAL', '0', 0),
(14, 1, 's2', 'strat2', 'TERMINAL', '0', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `StrategyToGoalList`
--

CREATE TABLE IF NOT EXISTS `StrategyToGoalList` (
  `strID` int(11) NOT NULL,
  `goalID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `StrategyToGoalList`
--

INSERT INTO `StrategyToGoalList` (`strID`, `goalID`) VALUES
(8, 9);

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
