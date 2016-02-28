-- phpMyAdmin SQL Dump
-- version 4.4.13.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Creato il: Feb 28, 2016 alle 13:39
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
-- Struttura della tabella `DefaultResponsible`
--

CREATE TABLE IF NOT EXISTS `DefaultResponsible` (
  `id` int(11) NOT NULL,
  `practitioner` int(11) NOT NULL,
  `className` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `DefaultResponsible`
--

INSERT INTO `DefaultResponsible` (`id`, `practitioner`, `className`) VALUES
(1, 2, 'pm');

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
  `state` int(2) NOT NULL DEFAULT '0',
  `timestamp` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Goal`
--

INSERT INTO `Goal` (`assumption`, `description`, `context`, `id`, `label`, `version`, `measurementGoal`, `state`, `timestamp`) VALUES
('', 'goal1', 'context1', 1, 'g1', 1, 2, 0, 1456662612527),
('', 'goal2', 'context2', 9, 'g2', 1, 10, 0, 1456662612525),
('', 'goal1', 'context1', 15, 'g1', 2, 2, 0, 1456662740489),
('', 'goal2', 'context2', 17, 'g2', 2, 18, 0, 1456662740488),
('', 'Goal rompiscatole', '', 24, 'g4534', 1, NULL, 0, 1456662741350),
('', 'goal1', 'context1', 25, 'g1', 3, 2, 0, 1456662741390),
('', 'goal2', 'context2', 27, 'g2', 3, 18, 0, 1456662741389),
('', 'Goal rompiscatole', '', 30, 'g4534', 2, NULL, 0, 1456662986940),
('', 'goal1', 'context1', 31, 'g1', 4, 2, 0, 1456662986952),
('', 'goal2', 'context2', 33, 'g2', 4, 34, 0, 1456662986951),
('', 'sono un nuovo main goal', '', 35, 'g9843', 1, NULL, 0, 1456662986782);

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
(15, 16),
(17, 13),
(17, 14),
(25, 26),
(27, 23),
(27, 21),
(31, 32),
(33, 29),
(33, 14);

-- --------------------------------------------------------

--
-- Struttura della tabella `Grid`
--

CREATE TABLE IF NOT EXISTS `Grid` (
  `id` int(11) NOT NULL,
  `version` int(10) NOT NULL,
  `projID` int(11) NOT NULL,
  `mainGoalsChanged` int(11) NOT NULL DEFAULT '0',
  `timestamp` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Grid`
--

INSERT INTO `Grid` (`id`, `version`, `projID`, `mainGoalsChanged`, `timestamp`) VALUES
(1, 1, 1, 0, 1456662612507),
(2, 2, 1, 0, 1456662740445),
(3, 3, 1, 0, 1456662740823),
(4, 4, 1, 1, 1456662986782);

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
(2, 15),
(2, 17),
(3, 25),
(3, 27),
(4, 31),
(4, 33),
(4, 35);

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
('GridElement', 1),
('Practitioner', 1),
('DefaultResponsible', 1);

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
  `state` int(2) NOT NULL DEFAULT '0',
  `timestamp` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `MeasurementGoal`
--

INSERT INTO `MeasurementGoal` (`id`, `label`, `version`, `description`, `interpretationModel`, `state`, `timestamp`) VALUES
(2, 'mg1', 1, 'meas goal 1', 'interpr1', 0, 1456662612518),
(10, 'mg2', 1, 'meas goal 2', 'interpr2', 0, 1456662612523),
(18, 'mg2', 2, 'meassssssssssss', 'un modello interpretativo', 0, 1456662740487),
(34, 'mg2', 3, 'this has to be a minor conflict', 'un modello interpretativo', 3, 1456662986782);

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
(18, 19),
(34, 19);

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
  `state` int(2) NOT NULL DEFAULT '2',
  `timestamp` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Metric`
--

INSERT INTO `Metric` (`count`, `description`, `measurementProcess`, `metricType`, `scaleType`, `label`, `id`, `version`, `state`, `timestamp`) VALUES
(0, 'm3 descr', 'm3 mProcess', 'BASE', 'm3 stype', 'm3', 4, 1, 0, 1456662612518),
(0, 'm2 descr', 'm2 mProcess', 'BASE', 'm2 stype', 'm2', 6, 1, 0, 1456662612517),
(0, 'm1 descr', 'm1 mProcess', 'BASE', 'm1 stype', 'm1', 7, 1, 0, 1456662612515),
(0, 'm4 descr', 'm4 mProcess', 'BASE', 'm4 stype', 'm4', 12, 1, 0, 1456662612518),
(0, 'modificata m4', 'm4 mProcess', 'BASE', 'Ordinale', 'm4', 20, 2, 0, 1456662740467);

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
  `name` char(100) NOT NULL,
  `email` char(100) DEFAULT NULL,
  `password` char(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Practitioner`
--

INSERT INTO `Practitioner` (`id`, `name`, `email`, `password`) VALUES
(1, 'Lorenzo La Banca', 'lorenzo.labanca@gmail.com', '$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y'),
(2, 'Paride Casulli', 'paride.casulli@gmail.com', '$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y');

-- --------------------------------------------------------

--
-- Struttura della tabella `Project`
--

CREATE TABLE IF NOT EXISTS `Project` (
  `id` int(11) NOT NULL,
  `projectId` char(100) DEFAULT NULL,
  `description` text,
  `creationDate` text,
  `projectManager` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Project`
--

INSERT INTO `Project` (`id`, `projectId`, `description`, `creationDate`, `projectManager`) VALUES
(1, 'progetto di prova', 'descrizione progetto', '18/02/2015 11:51', 2);

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
  `state` int(11) NOT NULL DEFAULT '2',
  `timestamp` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Question`
--

INSERT INTO `Question` (`id`, `label`, `version`, `question`, `state`, `timestamp`) VALUES
(3, 'q2', 1, 'question 2', 0, 1456662612518),
(5, 'q1', 1, 'question 1', 0, 1456662612519),
(11, 'q3', 1, 'question 3', 0, 1456662612523),
(19, 'q3', 2, 'question 3', 0, 1456662740467);

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
(19, 20);

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
  `state` int(2) NOT NULL DEFAULT '0',
  `timestamp` bigint(20) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `Strategy`
--

INSERT INTO `Strategy` (`id`, `version`, `label`, `description`, `strategyType`, `strategicProjectId`, `state`, `timestamp`) VALUES
(8, 1, 's1', 'strat1', 'NONTERMINAL', '0', 0, 1456662612524),
(13, 1, 's3', 'strat3', 'TERMINAL', '0', 0, 1456662612525),
(14, 1, 's2', 'this has to be a major update', 'TERMINAL', '0', 0, 1456662612525),
(16, 2, 's1', 'strat1', 'NONTERMINAL', '0', 0, 1456662740489),
(21, 2, 's2', 'strat2', 'TERMINAL', '0', 1, 1456662740823),
(22, 2, 's3', 'essetre', 'TERMINAL', '0', 5, 1456662740966),
(23, 4, 's3', 'essetre', 'TERMINAL', '0', 2, 1456662741349),
(26, 3, 's1', 'strat1', 'NONTERMINAL', '0', 0, 1456662741390),
(28, 2, 's3', 'this has to be a major conflict', 'TERMINAL', '0', 5, 1456662986796),
(29, 5, 's3', 'this has to be a major conflict', 'TERMINAL', '0', 2, 1456662986933),
(32, 4, 's1', 'strat1', 'NONTERMINAL', '0', 0, 1456662986952);

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
(8, 9),
(16, 17),
(23, 24),
(26, 27),
(29, 30),
(32, 33);

-- --------------------------------------------------------

--
-- Struttura della tabella `SubscriberPhase`
--

CREATE TABLE IF NOT EXISTS `SubscriberPhase` (
  `id` int(11) NOT NULL,
  `phase` int(11) NOT NULL,
  `url` text NOT NULL,
  `aProject` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `user_roles`
--

CREATE TABLE IF NOT EXISTS `user_roles` (
  `user_role_id` int(11) NOT NULL,
  `userid` int(11) NOT NULL,
  `role` varchar(45) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `user_roles`
--

INSERT INTO `user_roles` (`user_role_id`, `userid`, `role`) VALUES
(1, 1, 'ROLE_USER'),
(2, 2, 'ROLE_USER');

--
-- Indici per le tabelle scaricate
--

--
-- Indici per le tabelle `DefaultResponsible`
--
ALTER TABLE `DefaultResponsible`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `className` (`className`);

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

--
-- Indici per le tabelle `SubscriberPhase`
--
ALTER TABLE `SubscriberPhase`
  ADD PRIMARY KEY (`id`);

--
-- Indici per le tabelle `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_role_id`),
  ADD UNIQUE KEY `uni_username_role` (`role`,`userid`);

--
-- AUTO_INCREMENT per le tabelle scaricate
--

--
-- AUTO_INCREMENT per la tabella `user_roles`
--
ALTER TABLE `user_roles`
  MODIFY `user_role_id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=3;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
