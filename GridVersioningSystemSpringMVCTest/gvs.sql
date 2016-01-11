-- phpMyAdmin SQL Dump
-- version 4.4.13.1deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Creato il: Gen 11, 2016 alle 18:55
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
('assumption1', 'goal1', 'context1', 131072, 'g1', 1, 131073, 0),
('assumption2', 'goal2', 'context2', 131080, 'g2', 1, 131081, 0),
('assumption1', 'goal1', 'context1', 196608, 'g1', 2, 131073, 0),
('assumption2', 'modificatoTEST', 'context2', 196610, 'g2', 1, 131081, 0),
('assumption1', 'goal1', 'context1', 229376, 'g1', 3, 131073, 0),
('assumption2', 'modificatoTESTSpringMVC', 'context2', 229378, 'g2', 1, 131081, 0),
('assumption1', 'goal1', 'context1', 262144, 'g1', 4, 131073, 0),
('assumption2', 'modificatoTESTSpringMVC', 'context2', 262146, 'g2', 1, 131081, 0),
('assumption1', 'goal1', 'context1', 294912, 'g1', 5, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 294914, 'g2', 1, 131081, 0),
('assumption1', 'goal1', 'context1', 327680, 'g1', 6, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 327682, 'g2', 2, 131081, 0),
('assumption1', 'goal1', 'context1', 360448, 'g1', 7, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 360450, 'g2', 3, 131081, 0),
('assumption1', 'goal1', 'context1', 393216, 'g1', 8, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 393218, 'g2', 4, 131081, 0),
('assumption1', 'goal1', 'context1', 393219, 'g1', 9, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 393221, 'g2', 5, 131081, 0),
('assumption1', 'goal1', 'context1', 425984, 'g1', 10, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 425986, 'g2', 6, 131081, 0),
('assumption1', 'goal1', 'context1', 458752, 'g1', 11, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 458754, 'g2', 7, 131081, 0),
('assumption1', 'goal1', 'context1', 491520, 'g1', 12, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 491522, 'g2', 8, 131081, 0),
('assumption1', 'goal1', 'context1', 524288, 'g1', 13, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 524290, 'g2', 9, 131081, 0),
('assumption1', 'goal1', 'context1', 557056, 'g1', 14, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 557058, 'g2', 10, 131081, 0),
('assumption1', 'goal1', 'context1', 589824, 'g1', 15, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 589826, 'g2', 11, 131081, 0),
('assumption1', 'goal1', 'context1', 622592, 'g1', 16, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 622594, 'g2', 12, 131081, 0),
('assumption1', 'goal1', 'context1', 655360, 'g1', 17, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 655362, 'g2', 13, 131081, 0),
('assumption1', 'goal1', 'context1', 688128, 'g1', 18, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 688130, 'g2', 14, 131081, 0),
('assumption1', 'goal1', 'context1', 720896, 'g1', 19, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 720898, 'g2', 15, 131081, 0),
('assumption1', 'goal1', 'context1', 753664, 'g1', 20, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 753666, 'g2', 16, 131081, 0),
('assumption1', 'goal1', 'context1', 786432, 'g1', 21, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 786434, 'g2', 17, 131081, 0),
('assumption1', 'goal1', 'context1', 819200, 'g1', 22, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 819202, 'g2', 18, 131081, 0),
('assumption1', 'goal1', 'context1', 851968, 'g1', 23, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 851970, 'g2', 19, 131081, 0),
('assumption1', 'goal1', 'context1', 884736, 'g1', 24, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 884738, 'g2', 20, 131081, 0),
('assumption1', 'goal1', 'context1', 917504, 'g1', 25, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 917506, 'g2', 21, 131081, 0),
('assumption1', 'goal1', 'context1', 950272, 'g1', 26, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 950274, 'g2', 22, 131081, 0),
('assumption1', 'goal1', 'context1', 983040, 'g1', 27, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 983042, 'g2', 23, 131081, 0),
('assumption1', 'goal1', 'context1', 1015808, 'g1', 28, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 1015810, 'g2', 24, 131081, 0),
('assumption1', 'goal1', 'context1', 1048576, 'g1', 29, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 1048578, 'g2', 25, 131081, 0),
('assumption1', 'goal1', 'context1', 1048579, 'g1', 30, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante stocazzo', 'context2', 1048581, 'g2', 25, 131081, 0),
('assumption1', 'goal1', 'context1', 1081344, 'g1', 31, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 1081346, 'g2', 26, 131081, 0),
('assumption1', 'goal1', 'context1', 1114112, 'g1', 32, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 1114114, 'g2', 26, 131081, 0),
('assumption1', 'goal1', 'context1', 1146880, 'g1', 33, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 1146882, 'g2', 26, 131081, 0),
('assumption1', 'goal1', 'context1', 1179648, 'g1', 34, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 1179650, 'g2', 26, 131081, 0),
('assumption1', 'goal1', 'context1', 1212416, 'g1', 35, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante', 'context2', 1212418, 'g2', 26, 131081, 0),
('assumption1', 'goal1', 'context1', 1245184, 'g1', 36, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante27', 'context2', 1245186, 'g2', 27, 131081, 0),
('assumption1', 'goal1', 'context1', 1277952, 'g1', 37, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante28', 'context2', 1277954, 'g2', 28, 131081, 0),
('assumption1', 'goal1', 'context1', 1310720, 'g1', 38, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante29', 'context2', 1310722, 'g2', 29, 131081, 0),
('assumption1', 'goal1', 'context1', 1343488, 'g1', 39, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante30', 'context2', 1343490, 'g2', 30, 131081, 0),
('assumption1', 'goal1', 'context1', 1376256, 'g1', 40, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante31', 'context2', 1376258, 'g2', 31, 131081, 0),
('assumption1', 'goal1', 'context1', 1409024, 'g1', 41, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante32', 'context2', 1409026, 'g2', 32, 131081, 0),
('assumption1', 'goal1', 'context1', 1441792, 'g1', 42, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante33', 'context2', 1441794, 'g2', 33, 131081, 0),
('assumption1', 'goal1', 'context1', 1474560, 'g1', 43, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante34', 'context2', 1474562, 'g2', 34, 131081, 0),
('assumption1', 'goal1', 'context1', 1507328, 'g1', 44, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante35', 'context2', 1507330, 'g2', 35, 131081, 0),
('assumption1', 'goal1', 'context1', 1540096, 'g1', 45, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante36', 'context2', 1540098, 'g2', 36, 131081, 0),
('assumption1', 'goal1', 'context1', 1572864, 'g1', 46, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante37', 'context2', 1572866, 'g2', 37, 131081, 0),
('assumption1', 'goal1', 'context1', 1605632, 'g1', 47, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante38', 'context2', 1605634, 'g2', 38, 131081, 0),
('assumption1', 'goal1', 'context1', 1638400, 'g1', 48, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante39', 'context2', 1638402, 'g2', 39, 131081, 0),
('assumption1', 'goal1', 'context1', 1671168, 'g1', 49, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante40', 'context2', 1671170, 'g2', 40, 131081, 0),
('assumption1', 'goal1', 'context1', 1703936, 'g1', 50, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante41', 'context2', 1703938, 'g2', 41, 131081, 0),
('assumption1', 'goal1', 'context1', 1736704, 'g1', 51, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante42', 'context2', 1736706, 'g2', 42, 131081, 0),
('assumption1', 'goal1', 'context1', 1769472, 'g1', 52, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante43', 'context2', 1769474, 'g2', 43, 131081, 0),
('assumption1', 'goal1', 'context1', 1802240, 'g1', 53, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante44', 'context2', 1802242, 'g2', 44, 131081, 0),
('assumption1', 'goal1', 'context1', 1835008, 'g1', 54, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante45', 'context2', 1835010, 'g2', 45, 131081, 0),
('assumption1', 'goal1', 'context1', 1867776, 'g1', 55, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante46', 'context2', 1867778, 'g2', 46, 131081, 0),
('assumption1', 'goal1', 'context1', 1900544, 'g1', 56, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante47', 'context2', 1900546, 'g2', 47, 131081, 0),
('assumption1', 'goal1', 'context1', 1933312, 'g1', 57, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante48', 'context2', 1933314, 'g2', 48, 131081, 0),
('assumption1', 'goal1', 'context1', 1966080, 'g1', 58, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante49', 'context2', 1966082, 'g2', 49, 131081, 0),
('assumption1', 'goal1', 'context1', 1998848, 'g1', 59, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante50', 'context2', 1998850, 'g2', 50, 131081, 0),
('assumption1', 'goal1', 'context1', 2031616, 'g1', 60, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante51', 'context2', 2031618, 'g2', 51, 131081, 0),
('assumption1', 'goal1', 'context1', 2064384, 'g1', 61, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante52', 'context2', 2064386, 'g2', 52, 131081, 0),
('assumption1', 'goal1', 'context1', 2097152, 'g1', 62, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante53', 'context2', 2097154, 'g2', 53, 131081, 0),
('assumption1', 'goal1', 'context1', 2129920, 'g1', 63, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54', 'context2', 2129922, 'g2', 54, 131081, 0),
('assumption1', 'goal1', 'context1', 2162688, 'g1', 64, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*', 'context2', 2162690, 'g2', 55, 131081, 0),
('assumption1', 'goal1', 'context1', 2195456, 'g1', 65, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54**', 'context2', 2195458, 'g2', 56, 131081, 0),
('assumption1', 'goal1', 'context1', 2228224, 'g1', 66, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54***', 'context2', 2228226, 'g2', 57, 131081, 0),
('assumption1', 'goal1', 'context1', 2260992, 'g1', 67, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54****', 'context2', 2260994, 'g2', 58, 131081, 0),
('assumption1', 'goal1', 'context1', 2293760, 'g1', 68, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*****', 'context2', 2293762, 'g2', 59, 131081, 0),
('assumption1', 'goal1', 'context1', 2326528, 'g1', 69, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54******', 'context2', 2326530, 'g2', 60, 131081, 0),
('assumption1', 'goal1', 'context1', 2359296, 'g1', 70, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*******', 'context2', 2359298, 'g2', 61, 131081, 0),
('assumption1', 'goal1', 'context1', 2392064, 'g1', 71, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54********', 'context2', 2392066, 'g2', 62, 131081, 0),
('assumption1', 'goal1', 'context1', 2424832, 'g1', 72, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*********', 'context2', 2424834, 'g2', 63, 131081, 0),
('assumption1', 'goal1', 'context1', 2457600, 'g1', 73, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54**********', 'context2', 2457602, 'g2', 64, 131081, 0),
('assumption1', 'goal1', 'context1', 2490368, 'g1', 74, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54***********', 'context2', 2490370, 'g2', 65, 131081, 0),
('assumption1', 'goal1', 'context1', 2523136, 'g1', 75, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54************', 'context2', 2523138, 'g2', 66, 131081, 0),
('assumption1', 'goal1', 'context1', 2555904, 'g1', 76, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*************', 'context2', 2555906, 'g2', 67, 131081, 0),
('assumption1', 'goal1', 'context1', 2588672, 'g1', 77, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54**************', 'context2', 2588674, 'g2', 68, 131081, 0),
('assumption1', 'goal1', 'context1', 2621440, 'g1', 78, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54***************', 'context2', 2621442, 'g2', 69, 131081, 0),
('assumption1', 'goal1', 'context1', 2654208, 'g1', 79, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54****************', 'context2', 2654210, 'g2', 70, 131081, 0),
('assumption1', 'goal1', 'context1', 2686976, 'g1', 80, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*****************', 'context2', 2686978, 'g2', 71, 131081, 0),
('assumption1', 'goal1', 'context1', 2719744, 'g1', 81, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54******************', 'context2', 2719746, 'g2', 72, 131081, 0),
('assumption1', 'goal1', 'context1', 2752512, 'g1', 82, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*******************', 'context2', 2752514, 'g2', 73, 131081, 0),
('assumption1', 'goal1', 'context1', 2785280, 'g1', 83, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54********************', 'context2', 2785282, 'g2', 74, 131081, 0),
('assumption1', 'goal1', 'context1', 2818048, 'g1', 84, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*********************', 'context2', 2818050, 'g2', 75, 131081, 0),
('assumption1', 'goal1', 'context1', 2850816, 'g1', 85, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54**********************', 'context2', 2850818, 'g2', 76, 131081, 0),
('assumption1', 'goal1', 'context1', 2883584, 'g1', 86, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54***********************', 'context2', 2883586, 'g2', 77, 131081, 0),
('assumption1', 'goal1', 'context1', 2916352, 'g1', 87, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54************************', 'context2', 2916354, 'g2', 78, 131081, 0),
('assumption1', 'goal1', 'context1', 2949120, 'g1', 88, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*************************', 'context2', 2949122, 'g2', 79, 131081, 0),
('assumption1', 'goal1', 'context1', 2981888, 'g1', 89, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54**************************', 'context2', 2981890, 'g2', 80, 131081, 0),
('assumption1', 'goal1', 'context1', 3014656, 'g1', 90, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54***************************', 'context2', 3014658, 'g2', 81, 131081, 0),
('assumption1', 'goal1', 'context1', 3047424, 'g1', 91, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54****************************', 'context2', 3047426, 'g2', 82, 131081, 0),
('assumption1', 'goal1', 'context1', 3080192, 'g1', 92, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*****************************', 'context2', 3080194, 'g2', 83, 131081, 0),
('assumption1', 'goal1', 'context1', 3112960, 'g1', 93, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54******************************', 'context2', 3112962, 'g2', 84, 131081, 0),
('assumption1', 'goal1', 'context1', 3145728, 'g1', 94, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*******************************', 'context2', 3145730, 'g2', 85, 131081, 0),
('assumption1', 'goal1', 'context1', 3178496, 'g1', 95, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54********************************', 'context2', 3178498, 'g2', 86, 131081, 0),
('assumption1', 'goal1', 'context1', 3211264, 'g1', 96, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*********************************', 'context2', 3211266, 'g2', 87, 131081, 0),
('assumption1', 'goal1', 'context1', 3244032, 'g1', 97, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54**********************************', 'context2', 3244034, 'g2', 88, 131081, 0),
('assumption1', 'goal1', 'context1', 3276800, 'g1', 98, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54***********************************', 'context2', 3276802, 'g2', 89, 131081, 0),
('assumption1', 'goal1', 'context1', 3309568, 'g1', 99, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54************************************', 'context2', 3309570, 'g2', 90, 131081, 0),
('assumption1', 'goal1', 'context1', 3375104, 'g1', 100, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54*************************************', 'context2', 3375106, 'g2', 91, 131081, 1),
('assumption1', 'goal1', 'context1', 3407872, 'g1', 101, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54**************************************', 'context2', 3407874, 'g2', 92, 131081, 0),
('assumption1', 'goal1', 'context1', 3440640, 'g1', 102, 131073, 0),
('assumption2', 'modificatoTESTSpringMVCFunzionante54***************************************', 'context2', 3440642, 'g2', 93, 131081, 0);

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
(131072, 131079),
(131080, 131084),
(131080, 131085),
(196608, 196609),
(196610, 131084),
(196610, 131085),
(229376, 229377),
(229378, 131084),
(229378, 131085),
(262144, 262145),
(262146, 131084),
(262146, 131085),
(294912, 294913),
(294914, 131084),
(294914, 131085),
(327680, 327681),
(327682, 131084),
(327682, 131085),
(360448, 360449),
(360450, 131084),
(360450, 131085),
(393216, 393217),
(393218, 131084),
(393218, 131085),
(393219, 393220),
(393221, 131084),
(393221, 131085),
(425984, 425985),
(425986, 131084),
(425986, 131085),
(458752, 458753),
(458754, 131084),
(458754, 131085),
(491520, 491521),
(491522, 131084),
(491522, 131085),
(524288, 524289),
(524290, 131084),
(524290, 131085),
(557056, 557057),
(557058, 131084),
(557058, 131085),
(589824, 589825),
(589826, 131084),
(589826, 131085),
(622592, 622593),
(622594, 131084),
(622594, 131085),
(655360, 655361),
(655362, 131084),
(655362, 131085),
(688128, 688129),
(688130, 131084),
(688130, 131085),
(720896, 720897),
(720898, 131084),
(720898, 131085),
(753664, 753665),
(753666, 131084),
(753666, 131085),
(786432, 786433),
(786434, 131084),
(786434, 131085),
(819200, 819201),
(819202, 131084),
(819202, 131085),
(851968, 851969),
(851970, 131084),
(851970, 131085),
(884736, 884737),
(884738, 131084),
(884738, 131085),
(917504, 917505),
(917506, 131084),
(917506, 131085),
(950272, 950273),
(950274, 131084),
(950274, 131085),
(983040, 983041),
(983042, 131084),
(983042, 131085),
(1015808, 1015809),
(1015810, 131084),
(1015810, 131085),
(1048576, 1048577),
(1048578, 131084),
(1048578, 131085),
(1048579, 1048580),
(1048581, 131084),
(1048581, 131085),
(1081344, 1081345),
(1081346, 131084),
(1081346, 131085),
(1114112, 1114113),
(1114114, 131084),
(1114114, 131085),
(1146880, 1146881),
(1146882, 131084),
(1146882, 131085),
(1179648, 1179649),
(1179650, 131084),
(1179650, 131085),
(1212416, 1212417),
(1212418, 131084),
(1212418, 131085),
(1245184, 1245185),
(1245186, 131084),
(1245186, 131085),
(1277952, 1277953),
(1277954, 131084),
(1277954, 131085),
(1310720, 1310721),
(1310722, 131084),
(1310722, 131085),
(1343488, 1343489),
(1343490, 131084),
(1343490, 131085),
(1376256, 1376257),
(1376258, 131084),
(1376258, 131085),
(1409024, 1409025),
(1409026, 131084),
(1409026, 131085),
(1441792, 1441793),
(1441794, 131084),
(1441794, 131085),
(1474560, 1474561),
(1474562, 131084),
(1474562, 131085),
(1507328, 1507329),
(1507330, 131084),
(1507330, 131085),
(1540096, 1540097),
(1540098, 131084),
(1540098, 131085),
(1572864, 1572865),
(1572866, 131084),
(1572866, 131085),
(1605632, 1605633),
(1605634, 131084),
(1605634, 131085),
(1638400, 1638401),
(1638402, 131084),
(1638402, 131085),
(1671168, 1671169),
(1671170, 131084),
(1671170, 131085),
(1703936, 1703937),
(1703938, 131084),
(1703938, 131085),
(1736704, 1736705),
(1736706, 131084),
(1736706, 131085),
(1769472, 1769473),
(1769474, 131084),
(1769474, 131085),
(1802240, 1802241),
(1802242, 131084),
(1802242, 131085),
(1835008, 1835009),
(1835010, 131084),
(1835010, 131085),
(1867776, 1867777),
(1867778, 131084),
(1867778, 131085),
(1900544, 1900545),
(1900546, 131084),
(1900546, 131085),
(1933312, 1933313),
(1933314, 131084),
(1933314, 131085),
(1966080, 1966081),
(1966082, 131084),
(1966082, 131085),
(1998848, 1998849),
(1998850, 131084),
(1998850, 131085),
(2031616, 2031617),
(2031618, 131084),
(2031618, 131085),
(2064384, 2064385),
(2064386, 131084),
(2064386, 131085),
(2097152, 2097153),
(2097154, 131084),
(2097154, 131085),
(2129920, 2129921),
(2129922, 131084),
(2129922, 131085),
(2162688, 2162689),
(2162690, 131084),
(2162690, 131085),
(2195456, 2195457),
(2195458, 131084),
(2195458, 131085),
(2228224, 2228225),
(2228226, 131084),
(2228226, 131085),
(2260992, 2260993),
(2260994, 131084),
(2260994, 131085),
(2293760, 2293761),
(2293762, 131084),
(2293762, 131085),
(2326528, 2326529),
(2326530, 131084),
(2326530, 131085),
(2359296, 2359297),
(2359298, 131084),
(2359298, 131085),
(2392064, 2392065),
(2392066, 131084),
(2392066, 131085),
(2424832, 2424833),
(2424834, 131084),
(2424834, 131085),
(2457600, 2457601),
(2457602, 131084),
(2457602, 131085),
(2490368, 2490369),
(2490370, 131084),
(2490370, 131085),
(2523136, 2523137),
(2523138, 131084),
(2523138, 131085),
(2555904, 2555905),
(2555906, 131084),
(2555906, 131085),
(2588672, 2588673),
(2588674, 131084),
(2588674, 131085),
(2621440, 2621441),
(2621442, 131084),
(2621442, 131085),
(2654208, 2654209),
(2654210, 131084),
(2654210, 131085),
(2686976, 2686977),
(2686978, 131084),
(2686978, 131085),
(2719744, 2719745),
(2719746, 131084),
(2719746, 131085),
(2752512, 2752513),
(2752514, 131084),
(2752514, 131085),
(2785280, 2785281),
(2785282, 131084),
(2785282, 131085),
(2818048, 2818049),
(2818050, 131084),
(2818050, 131085),
(2850816, 2850817),
(2850818, 131084),
(2850818, 131085),
(2883584, 2883585),
(2883586, 131084),
(2883586, 131085),
(2916352, 2916353),
(2916354, 131084),
(2916354, 131085),
(2949120, 2949121),
(2949122, 131084),
(2949122, 131085),
(2981888, 2981889),
(2981890, 131084),
(2981890, 131085),
(3014656, 3014657),
(3014658, 131084),
(3014658, 131085),
(3047424, 3047425),
(3047426, 131084),
(3047426, 131085),
(3080192, 3080193),
(3080194, 131084),
(3080194, 131085),
(3112960, 3112961),
(3112962, 131084),
(3112962, 131085),
(3145728, 3145729),
(3145730, 131084),
(3145730, 131085),
(3178496, 3178497),
(3178498, 131084),
(3178498, 131085),
(3211264, 3211265),
(3211266, 131084),
(3211266, 131085),
(3244032, 3244033),
(3244034, 131084),
(3244034, 131085),
(3276800, 3276801),
(3276802, 131084),
(3276802, 131085),
(3309568, 3309569),
(3309570, 131084),
(3309570, 131085),
(3375104, 3375105),
(3375106, 131084),
(3375106, 131085),
(3407872, 3407873),
(3407874, 131084),
(3407874, 131085),
(3440640, 3440641),
(3440642, 131084),
(3440642, 131085);

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
(131072, 1, 131072),
(196608, 2, 131072),
(229376, 3, 131072),
(262144, 4, 131072),
(294912, 5, 131072),
(327680, 6, 131072),
(360448, 7, 131072),
(393216, 8, 131072),
(393217, 9, 131072),
(425984, 10, 131072),
(458752, 11, 131072),
(491520, 12, 131072),
(524288, 13, 131072),
(557056, 14, 131072),
(589824, 15, 131072),
(622592, 16, 131072),
(655360, 17, 131072),
(688128, 18, 131072),
(720896, 19, 131072),
(753664, 20, 131072),
(786432, 21, 131072),
(819200, 22, 131072),
(851968, 23, 131072),
(884736, 24, 131072),
(917504, 25, 131072),
(950272, 26, 131072),
(983040, 27, 131072),
(1015808, 28, 131072),
(1048576, 29, 131072),
(1048577, 30, 131072),
(1081344, 31, 131072),
(1114112, 32, 131072),
(1146880, 33, 131072),
(1179648, 34, 131072),
(1212416, 35, 131072),
(1245184, 36, 131072),
(1277952, 37, 131072),
(1310720, 38, 131072),
(1343488, 39, 131072),
(1376256, 40, 131072),
(1409024, 41, 131072),
(1441792, 42, 131072),
(1474560, 43, 131072),
(1507328, 44, 131072),
(1540096, 45, 131072),
(1572864, 46, 131072),
(1605632, 47, 131072),
(1638400, 48, 131072),
(1671168, 49, 131072),
(1703936, 50, 131072),
(1736704, 51, 131072),
(1769472, 52, 131072),
(1802240, 53, 131072),
(1835008, 54, 131072),
(1867776, 55, 131072),
(1900544, 56, 131072),
(1933312, 57, 131072),
(1966080, 58, 131072),
(1998848, 59, 131072),
(2031616, 60, 131072),
(2064384, 61, 131072),
(2097152, 62, 131072),
(2129920, 63, 131072),
(2162688, 64, 131072),
(2195456, 65, 131072),
(2228224, 66, 131072),
(2260992, 67, 131072),
(2293760, 68, 131072),
(2326528, 69, 131072),
(2359296, 70, 131072),
(2392064, 71, 131072),
(2424832, 72, 131072),
(2457600, 73, 131072),
(2490368, 74, 131072),
(2523136, 75, 131072),
(2555904, 76, 131072),
(2588672, 77, 131072),
(2621440, 78, 131072),
(2654208, 79, 131072),
(2686976, 80, 131072),
(2719744, 81, 131072),
(2752512, 82, 131072),
(2785280, 83, 131072),
(2818048, 84, 131072),
(2850816, 85, 131072),
(2883584, 86, 131072),
(2916352, 87, 131072),
(2949120, 88, 131072),
(2981888, 89, 131072),
(3014656, 90, 131072),
(3047424, 91, 131072),
(3080192, 92, 131072),
(3112960, 93, 131072),
(3145728, 94, 131072),
(3178496, 95, 131072),
(3211264, 96, 131072),
(3244032, 97, 131072),
(3276800, 98, 131072),
(3309568, 99, 131072),
(3375104, 100, 131072),
(3407872, 101, 131072),
(3440640, 102, 131072);

-- --------------------------------------------------------

--
-- Struttura della tabella `GridElementToAuthors`
--

CREATE TABLE IF NOT EXISTS `GridElementToAuthors` (
  `gridElementID` int(11) NOT NULL,
  `author` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `GridElementToAuthors`
--

INSERT INTO `GridElementToAuthors` (`gridElementID`, `author`) VALUES
(3375106, 'paride.casulli@gmail.com');

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
(131072, 131072),
(131072, 131080),
(196608, 196608),
(196608, 196610),
(229376, 229376),
(229376, 229378),
(262144, 262144),
(262144, 262146),
(294912, 294912),
(294912, 294914),
(327680, 327680),
(327680, 327682),
(360448, 360448),
(360448, 360450),
(393216, 393216),
(393216, 393218),
(393217, 393219),
(393217, 393221),
(425984, 425984),
(425984, 425986),
(458752, 458752),
(458752, 458754),
(491520, 491520),
(491520, 491522),
(524288, 524288),
(524288, 524290),
(557056, 557056),
(557056, 557058),
(589824, 589824),
(589824, 589826),
(622592, 622592),
(622592, 622594),
(655360, 655360),
(655360, 655362),
(688128, 688128),
(688128, 688130),
(720896, 720896),
(720896, 720898),
(753664, 753664),
(753664, 753666),
(786432, 786432),
(786432, 786434),
(819200, 819200),
(819200, 819202),
(851968, 851968),
(851968, 851970),
(884736, 884736),
(884736, 884738),
(917504, 917504),
(917504, 917506),
(950272, 950272),
(950272, 950274),
(983040, 983040),
(983040, 983042),
(1015808, 1015808),
(1015808, 1015810),
(1048576, 1048576),
(1048576, 1048578),
(1048577, 1048579),
(1048577, 1048581),
(1081344, 1081344),
(1081344, 1081346),
(1114112, 1114112),
(1114112, 1114114),
(1146880, 1146880),
(1146880, 1146882),
(1179648, 1179648),
(1179648, 1179650),
(1212416, 1212416),
(1212416, 1212418),
(1245184, 1245184),
(1245184, 1245186),
(1277952, 1277952),
(1277952, 1277954),
(1310720, 1310720),
(1310720, 1310722),
(1343488, 1343488),
(1343488, 1343490),
(1376256, 1376256),
(1376256, 1376258),
(1409024, 1409024),
(1409024, 1409026),
(1441792, 1441792),
(1441792, 1441794),
(1474560, 1474560),
(1474560, 1474562),
(1507328, 1507328),
(1507328, 1507330),
(1540096, 1540096),
(1540096, 1540098),
(1572864, 1572864),
(1572864, 1572866),
(1605632, 1605632),
(1605632, 1605634),
(1638400, 1638400),
(1638400, 1638402),
(1671168, 1671168),
(1671168, 1671170),
(1703936, 1703936),
(1703936, 1703938),
(1736704, 1736704),
(1736704, 1736706),
(1769472, 1769472),
(1769472, 1769474),
(1802240, 1802240),
(1802240, 1802242),
(1835008, 1835008),
(1835008, 1835010),
(1867776, 1867776),
(1867776, 1867778),
(1900544, 1900544),
(1900544, 1900546),
(1933312, 1933312),
(1933312, 1933314),
(1966080, 1966080),
(1966080, 1966082),
(1998848, 1998848),
(1998848, 1998850),
(2031616, 2031616),
(2031616, 2031618),
(2064384, 2064384),
(2064384, 2064386),
(2097152, 2097152),
(2097152, 2097154),
(2129920, 2129920),
(2129920, 2129922),
(2162688, 2162688),
(2162688, 2162690),
(2195456, 2195456),
(2195456, 2195458),
(2228224, 2228224),
(2228224, 2228226),
(2260992, 2260992),
(2260992, 2260994),
(2293760, 2293760),
(2293760, 2293762),
(2326528, 2326528),
(2326528, 2326530),
(2359296, 2359296),
(2359296, 2359298),
(2392064, 2392064),
(2392064, 2392066),
(2424832, 2424832),
(2424832, 2424834),
(2457600, 2457600),
(2457600, 2457602),
(2490368, 2490368),
(2490368, 2490370),
(2523136, 2523136),
(2523136, 2523138),
(2555904, 2555904),
(2555904, 2555906),
(2588672, 2588672),
(2588672, 2588674),
(2621440, 2621440),
(2621440, 2621442),
(2654208, 2654208),
(2654208, 2654210),
(2686976, 2686976),
(2686976, 2686978),
(2719744, 2719744),
(2719744, 2719746),
(2752512, 2752512),
(2752512, 2752514),
(2785280, 2785280),
(2785280, 2785282),
(2818048, 2818048),
(2818048, 2818050),
(2850816, 2850816),
(2850816, 2850818),
(2883584, 2883584),
(2883584, 2883586),
(2916352, 2916352),
(2916352, 2916354),
(2949120, 2949120),
(2949120, 2949122),
(2981888, 2981888),
(2981888, 2981890),
(3014656, 3014656),
(3014656, 3014658),
(3047424, 3047424),
(3047424, 3047426),
(3080192, 3080192),
(3080192, 3080194),
(3112960, 3112960),
(3112960, 3112962),
(3145728, 3145728),
(3145728, 3145730),
(3178496, 3178496),
(3178496, 3178498),
(3211264, 3211264),
(3211264, 3211266),
(3244032, 3244032),
(3244032, 3244034),
(3276800, 3276800),
(3276800, 3276802),
(3309568, 3309568),
(3309568, 3309570),
(3375104, 3375104),
(3375104, 3375106),
(3407872, 3407872),
(3407872, 3407874),
(3440640, 3440640),
(3440640, 3440642);

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
('Grid', 106),
('Project', 5),
('GridElement', 106);

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
(131073, 'mg1', 1, 'meas goal 1', 'interpr1', 0),
(131081, 'mg2', 1, 'meas goal 2', 'interpr2', 0);

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
(131073, 131074),
(131073, 131076),
(131081, 131082);

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
(0, 'm3 descr', 'm3 mProcess', 'BASE', 'm3 stype', 'm3', 131075, 1, 0),
(0, 'm2 descr', 'm2 mProcess', 'BASE', 'm2 stype', 'm2', 131077, 1, 0),
(0, 'm1 descr', 'm1 mProcess', 'BASE', 'm1 stype', 'm1', 131078, 1, 0),
(0, 'm4 descr', 'm4 mProcess', 'BASE', 'm4 stype', 'm4', 131083, 1, 0);

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
(131072, 'progetto di prova', 'descrizione progetto', '18/02/2015 11:51');

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
(131074, 'q2', 1, 'question 2', 0),
(131076, 'q1', 1, 'question 1', 0),
(131082, 'q3', 1, 'question 3', 0);

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
(131074, 131075),
(131076, 131077),
(131076, 131078),
(131082, 131083);

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

--
-- Dump dei dati per la tabella `Strategy`
--

INSERT INTO `Strategy` (`id`, `version`, `label`, `description`, `isTerminal`, `strategicProjectId`, `state`) VALUES
(131079, 1, 's1', 'non deve salvare questo', 0, '0', 0),
(131084, 1, 's3', 'strat3', 1, '0', 0),
(131085, 1, 's2', 'strat2', 1, '0', 0),
(196609, 2, 's1', 'strat1', 0, '0', 0),
(229377, 3, 's1', 'strat1', 0, '0', 0),
(262145, 4, 's1', 'strat1', 0, '0', 0),
(294913, 5, 's1', 'strat1', 0, '0', 0),
(327681, 6, 's1', 'strat1', 0, '0', 0),
(360449, 7, 's1', 'strat1', 0, '0', 0),
(393217, 8, 's1', 'strat1', 0, '0', 0),
(393220, 9, 's1', 'strat1', 0, '0', 0),
(425985, 10, 's1', 'strat1', 0, '0', 0),
(458753, 11, 's1', 'strat1', 0, '0', 0),
(491521, 12, 's1', 'strat1', 0, '0', 0),
(524289, 13, 's1', 'strat1', 0, '0', 0),
(557057, 14, 's1', 'strat1', 0, '0', 0),
(589825, 15, 's1', 'strat1', 0, '0', 0),
(622593, 16, 's1', 'strat1', 0, '0', 0),
(655361, 17, 's1', 'strat1', 0, '0', 0),
(688129, 18, 's1', 'strat1', 0, '0', 0),
(720897, 19, 's1', 'strat1', 0, '0', 0),
(753665, 20, 's1', 'strat1', 0, '0', 0),
(786433, 21, 's1', 'strat1', 0, '0', 0),
(819201, 22, 's1', 'strat1', 0, '0', 0),
(851969, 23, 's1', 'strat1', 0, '0', 0),
(884737, 24, 's1', 'strat1', 0, '0', 0),
(917505, 25, 's1', 'strat1', 0, '0', 0),
(950273, 26, 's1', 'strat1', 0, '0', 0),
(983041, 27, 's1', 'strat1', 0, '0', 0),
(1015809, 28, 's1', 'strat1', 0, '0', 0),
(1048577, 29, 's1', 'strat1', 0, '0', 0),
(1048580, 30, 's1', 'strat1', 0, '0', 0),
(1081345, 31, 's1', 'strat1', 0, '0', 0),
(1114113, 32, 's1', 'strat1', 0, '0', 0),
(1146881, 33, 's1', 'strat1', 0, '0', 0),
(1179649, 34, 's1', 'strat1', 0, '0', 0),
(1212417, 35, 's1', 'strat1', 0, '0', 0),
(1245185, 36, 's1', 'strat1', 0, '0', 0),
(1277953, 37, 's1', 'strat1', 0, '0', 0),
(1310721, 38, 's1', 'strat1', 0, '0', 0),
(1343489, 39, 's1', 'strat1', 0, '0', 0),
(1376257, 40, 's1', 'strat1', 0, '0', 0),
(1409025, 41, 's1', 'strat1', 0, '0', 0),
(1441793, 42, 's1', 'strat1', 0, '0', 0),
(1474561, 43, 's1', 'strat1', 0, '0', 0),
(1507329, 44, 's1', 'strat1', 0, '0', 0),
(1540097, 45, 's1', 'strat1', 0, '0', 0),
(1572865, 46, 's1', 'strat1', 0, '0', 0),
(1605633, 47, 's1', 'strat1', 0, '0', 0),
(1638401, 48, 's1', 'strat1', 0, '0', 0),
(1671169, 49, 's1', 'strat1', 0, '0', 0),
(1703937, 50, 's1', 'strat1', 0, '0', 0),
(1736705, 51, 's1', 'strat1', 0, '0', 0),
(1769473, 52, 's1', 'strat1', 0, '0', 0),
(1802241, 53, 's1', 'strat1', 0, '0', 0),
(1835009, 54, 's1', 'strat1', 0, '0', 0),
(1867777, 55, 's1', 'strat1', 0, '0', 0),
(1900545, 56, 's1', 'strat1', 0, '0', 0),
(1933313, 57, 's1', 'strat1', 0, '0', 0),
(1966081, 58, 's1', 'strat1', 0, '0', 0),
(1998849, 59, 's1', 'strat1', 0, '0', 0),
(2031617, 60, 's1', 'strat1', 0, '0', 0),
(2064385, 61, 's1', 'strat1', 0, '0', 0),
(2097153, 62, 's1', 'strat1', 0, '0', 0),
(2129921, 63, 's1', 'strat1', 0, '0', 0),
(2162689, 64, 's1', 'strat1', 0, '0', 0),
(2195457, 65, 's1', 'strat1', 0, '0', 0),
(2228225, 66, 's1', 'strat1', 0, '0', 0),
(2260993, 67, 's1', 'strat1', 0, '0', 0),
(2293761, 68, 's1', 'strat1', 0, '0', 0),
(2326529, 69, 's1', 'strat1', 0, '0', 0),
(2359297, 70, 's1', 'strat1', 0, '0', 0),
(2392065, 71, 's1', 'strat1', 0, '0', 0),
(2424833, 72, 's1', 'strat1', 0, '0', 0),
(2457601, 73, 's1', 'strat1', 0, '0', 0),
(2490369, 74, 's1', 'strat1', 0, '0', 0),
(2523137, 75, 's1', 'strat1', 0, '0', 0),
(2555905, 76, 's1', 'strat1', 0, '0', 0),
(2588673, 77, 's1', 'strat1', 0, '0', 0),
(2621441, 78, 's1', 'strat1', 0, '0', 0),
(2654209, 79, 's1', 'strat1', 0, '0', 0),
(2686977, 80, 's1', 'strat1', 0, '0', 0),
(2719745, 81, 's1', 'strat1', 0, '0', 0),
(2752513, 82, 's1', 'strat1', 0, '0', 0),
(2785281, 83, 's1', 'strat1', 0, '0', 0),
(2818049, 84, 's1', 'strat1', 0, '0', 0),
(2850817, 85, 's1', 'strat1', 0, '0', 0),
(2883585, 86, 's1', 'strat1', 0, '0', 0),
(2916353, 87, 's1', 'strat1', 0, '0', 0),
(2949121, 88, 's1', 'strat1', 0, '0', 0),
(2981889, 89, 's1', 'strat1', 0, '0', 0),
(3014657, 90, 's1', 'strat1', 0, '0', 0),
(3047425, 91, 's1', 'strat1', 0, '0', 0),
(3080193, 92, 's1', 'strat1', 0, '0', 0),
(3112961, 93, 's1', 'strat1', 0, '0', 0),
(3145729, 94, 's1', 'strat1', 0, '0', 0),
(3178497, 95, 's1', 'strat1', 0, '0', 0),
(3211265, 96, 's1', 'strat1', 0, '0', 0),
(3244033, 97, 's1', 'strat1', 0, '0', 0),
(3276801, 98, 's1', 'strat1', 0, '0', 0),
(3309569, 99, 's1', 'strat1', 0, '0', 0),
(3375105, 100, 's1', 'strat1', 0, '0', 0),
(3407873, 101, 's1', 'strat1', 0, '0', 0),
(3440641, 102, 's1', 'strat1', 0, '0', 0);

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
(131079, 131080),
(196609, 196610),
(229377, 229378),
(262145, 262146),
(294913, 294914),
(327681, 327682),
(360449, 360450),
(393217, 393218),
(393220, 393221),
(425985, 425986),
(458753, 458754),
(491521, 491522),
(524289, 524290),
(557057, 557058),
(589825, 589826),
(622593, 622594),
(655361, 655362),
(688129, 688130),
(720897, 720898),
(753665, 753666),
(786433, 786434),
(819201, 819202),
(851969, 851970),
(884737, 884738),
(917505, 917506),
(950273, 950274),
(983041, 983042),
(1015809, 1015810),
(1048577, 1048578),
(1048580, 1048581),
(1081345, 1081346),
(1114113, 1114114),
(1146881, 1146882),
(1179649, 1179650),
(1212417, 1212418),
(1245185, 1245186),
(1277953, 1277954),
(1310721, 1310722),
(1343489, 1343490),
(1376257, 1376258),
(1409025, 1409026),
(1441793, 1441794),
(1474561, 1474562),
(1507329, 1507330),
(1540097, 1540098),
(1572865, 1572866),
(1605633, 1605634),
(1638401, 1638402),
(1671169, 1671170),
(1703937, 1703938),
(1736705, 1736706),
(1769473, 1769474),
(1802241, 1802242),
(1835009, 1835010),
(1867777, 1867778),
(1900545, 1900546),
(1933313, 1933314),
(1966081, 1966082),
(1998849, 1998850),
(2031617, 2031618),
(2064385, 2064386),
(2097153, 2097154),
(2129921, 2129922),
(2162689, 2162690),
(2195457, 2195458),
(2228225, 2228226),
(2260993, 2260994),
(2293761, 2293762),
(2326529, 2326530),
(2359297, 2359298),
(2392065, 2392066),
(2424833, 2424834),
(2457601, 2457602),
(2490369, 2490370),
(2523137, 2523138),
(2555905, 2555906),
(2588673, 2588674),
(2621441, 2621442),
(2654209, 2654210),
(2686977, 2686978),
(2719745, 2719746),
(2752513, 2752514),
(2785281, 2785282),
(2818049, 2818050),
(2850817, 2850818),
(2883585, 2883586),
(2916353, 2916354),
(2949121, 2949122),
(2981889, 2981890),
(3014657, 3014658),
(3047425, 3047426),
(3080193, 3080194),
(3112961, 3112962),
(3145729, 3145730),
(3178497, 3178498),
(3211265, 3211266),
(3244033, 3244034),
(3276801, 3276802),
(3309569, 3309570),
(3375105, 3375106),
(3407873, 3407874),
(3440641, 3440642);

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
