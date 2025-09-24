-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Sep 24, 2025 at 10:50 AM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `furever`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbl_adopter`
--

CREATE TABLE `tbl_adopter` (
  `adopter_id` int(11) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `adopter_name` varchar(100) NOT NULL,
  `adopter_contact` varchar(15) DEFAULT NULL,
  `adopter_email` varchar(100) DEFAULT NULL,
  `adopter_address` varchar(255) DEFAULT NULL,
  `adopter_profile` text DEFAULT NULL,
  `adopter_username` varchar(50) DEFAULT NULL,
  `adopter_password` varchar(255) DEFAULT NULL,
  `archived` tinyint(1) NOT NULL DEFAULT 0,
  `archived_date` datetime DEFAULT NULL
) ;

--
-- Dumping data for table `tbl_adopter`
--

INSERT INTO `tbl_adopter` (`adopter_id`, `username`, `adopter_name`, `adopter_contact`, `adopter_email`, `adopter_address`, `adopter_profile`, `adopter_username`, `adopter_password`, `archived`, `archived_date`) VALUES
(1, 'alicej', 'Alice Johnson', '09171234567', 'alice@example.com', '123 Manila St.', NULL, 'alicej', 'password123', 0, NULL),
(2, 'markc', 'Mark Cruz', '0999999999', 'mark.cruz@example.com', '456 Quezon Ave.', NULL, 'markc', 'password123', 0, NULL),
(3, 'adopter1', 'John Adopter', '09694567234', 'john.adopter@example.com', '789 Pet Lover St., Quezon City', NULL, 'adopter1', 'iwantmydaddy', 0, NULL),
(4, 'ginathis', 'Theresa', '09693764567', 'theresa.gina@example.com', '92 g. marcelo st', 'n/a', 'ginathis', 'Sdfg123', 0, NULL),
(6, 'Adopter20', 'Test Adopter', '09123456789', 'adopter1@example.com', '123 Test Address, City', NULL, 'Adopter20', 'adoptme', 0, NULL),
(7, 'Lara Croft', 'Lara Croft', '09000000000', 'lara.croft@example.com', 'Tomb Raider Mansion', NULL, 'Lara Croft', 'dsfgerty', 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_adoption`
--

CREATE TABLE `tbl_adoption` (
  `adoption_id` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `adopter_id` int(11) NOT NULL,
  `adoption_date` date DEFAULT NULL,
  `upload_adoption_document` text DEFAULT NULL,
  `remarks` text DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `archived` tinyint(1) NOT NULL DEFAULT 0,
  `archived_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_adoption`
--

INSERT INTO `tbl_adoption` (`adoption_id`, `pet_id`, `adopter_id`, `adoption_date`, `upload_adoption_document`, `remarks`, `user_id`, `archived`, `archived_date`) VALUES
(1, 2, 2, '2025-09-15', NULL, 'Mittens officially adopted', NULL, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_adoption_request`
--

CREATE TABLE `tbl_adoption_request` (
  `adoption_request_id` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `adopter_id` int(11) NOT NULL,
  `request_date` date DEFAULT NULL,
  `status` enum('Pending','Approved','Rejected') DEFAULT 'Pending',
  `approval_date` date DEFAULT NULL,
  `remarks` text DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `archived` tinyint(1) NOT NULL DEFAULT 0,
  `archived_date` datetime DEFAULT NULL
) ;

--
-- Dumping data for table `tbl_adoption_request`
--

INSERT INTO `tbl_adoption_request` (`adoption_request_id`, `pet_id`, `adopter_id`, `request_date`, `status`, `approval_date`, `remarks`, `user_id`, `archived`, `archived_date`) VALUES
(1, 1, 1, '2025-09-10', 'Rejected', NULL, 'feral', NULL, 0, NULL),
(4, 2, 3, '2025-09-22', 'Rejected', NULL, 'Rejected - Adopter already has an approved request for this pet', NULL, 0, NULL),
(5, 2, 6, '2025-09-24', 'Approved', '2025-09-24', 'yesss', NULL, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_pet`
--

CREATE TABLE `tbl_pet` (
  `pet_id` int(11) NOT NULL,
  `pet_owner_id` int(11) NOT NULL,
  `pet_name` varchar(100) NOT NULL,
  `pet_type_id` int(11) NOT NULL,
  `description` text DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` enum('Male','Female') DEFAULT NULL,
  `health_status` enum('Healthy','Needs Treatment') DEFAULT NULL,
  `upload_health_history` text DEFAULT NULL,
  `vaccination_status` enum('Vaccinated','Not Vaccinated') DEFAULT NULL,
  `proof_of_vaccination` text DEFAULT NULL,
  `adoption_status` enum('Available','Pending','Adopted') NOT NULL DEFAULT 'Available',
  `date_registered` date NOT NULL DEFAULT curdate(),
  `archived` tinyint(1) NOT NULL DEFAULT 0,
  `archived_date` datetime DEFAULT NULL
) ;

--
-- Dumping data for table `tbl_pet`
--

INSERT INTO `tbl_pet` (`pet_id`, `pet_owner_id`, `pet_name`, `pet_type_id`, `description`, `age`, `gender`, `health_status`, `upload_health_history`, `vaccination_status`, `proof_of_vaccination`, `adoption_status`, `date_registered`, `archived`, `archived_date`) VALUES
(1, 1, 'Buddy', 1, 'Friendly golden retriever', 3, 'Male', 'Healthy', NULL, 'Vaccinated', NULL, 'Available', '2025-09-01', 0, NULL),
(2, 1, 'mitts', 2, 'Playful Persian cat', 2, 'Female', 'Healthy', NULL, 'Not Vaccinated', NULL, 'Adopted', '2025-09-05', 0, NULL),
(4, 1, 'Kazu', 1, 'musty', 4, 'Male', 'Healthy', NULL, 'Vaccinated', NULL, 'Adopted', '2024-10-20', 0, NULL),
(5, 1, 'Oni', 2, 'kkk', 3, 'Female', 'Healthy', NULL, NULL, NULL, 'Available', '2025-09-23', 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_pet_media`
--

CREATE TABLE `tbl_pet_media` (
  `pet_media_id` int(11) NOT NULL,
  `pet_id` int(11) NOT NULL,
  `pet_media_name` varchar(255) DEFAULT NULL,
  `pet_media_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_pet_media`
--

INSERT INTO `tbl_pet_media` (`pet_media_id`, `pet_id`, `pet_media_name`, `pet_media_url`) VALUES
(1, 1, 'Buddy Photo', 'uploads/buddy.jpg'),
(2, 2, 'Mittens Photo', 'uploads/mittens.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `tbl_pet_owner`
--

CREATE TABLE `tbl_pet_owner` (
  `pet_owner_id` int(11) NOT NULL,
  `pet_owner_name` varchar(100) NOT NULL,
  `pet_owner_contact` varchar(15) DEFAULT NULL,
  `pet_owner_email` varchar(100) DEFAULT NULL,
  `pet_owner_address` varchar(255) DEFAULT NULL,
  `pet_owner_profile` text DEFAULT NULL,
  `pet_owner_username` varchar(50) DEFAULT NULL,
  `pet_owner_password` varchar(255) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `archived` tinyint(1) NOT NULL DEFAULT 0,
  `archived_date` datetime DEFAULT NULL
) ;

--
-- Dumping data for table `tbl_pet_owner`
--

INSERT INTO `tbl_pet_owner` (`pet_owner_id`, `pet_owner_name`, `pet_owner_contact`, `pet_owner_email`, `pet_owner_address`, `pet_owner_profile`, `pet_owner_username`, `pet_owner_password`, `username`, `archived`, `archived_date`) VALUES
(1, 'Juanito Dela Cruz', '09181234567', 'juan@example.com', '789 Pasig Blvd.', NULL, 'juandc', 'password123', 'juandc', 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `tbl_pet_type`
--

CREATE TABLE `tbl_pet_type` (
  `pet_type_id` int(11) NOT NULL,
  `pet_type_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `tbl_pet_type`
--

INSERT INTO `tbl_pet_type` (`pet_type_id`, `pet_type_name`) VALUES
(1, 'Dog'),
(2, 'Cat');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','adopter','pet_owner') DEFAULT 'adopter',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `archived` tinyint(1) NOT NULL DEFAULT 0,
  `archived_date` datetime DEFAULT NULL
) ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `role`, `created_at`, `archived`, `archived_date`) VALUES
(1, 'admin12', 'admin1@example.com', 'admin123', 'admin', '2025-09-22 02:57:34', 0, NULL),
(2, 'Adopter20', 'adopter1@example.com', 'adoptme', 'adopter', '2025-09-22 02:57:34', 0, NULL),
(6, 'juandc', 'juan@example.com', 'password123', 'pet_owner', '2025-09-23 02:34:37', 0, NULL),
(14, 'alicej', 'alice@example.com', 'password123', 'adopter', '2025-09-24 08:45:30', 0, NULL),
(15, 'markc', 'mark@example.com', 'password123', 'adopter', '2025-09-24 08:45:30', 0, NULL),
(16, 'ginathis', 'ginatheresa@gmail.com', 'Sdfg123', 'adopter', '2025-09-24 08:45:30', 0, NULL),
(17, 'adopter1', 'john.adopter@example.com', 'iwantmydaddy', 'adopter', '2025-09-24 08:46:04', 0, NULL),
(21, 'Lara Croft', 'lara.croft@example.com', 'dsfgerty', 'adopter', '2025-09-24 08:46:04', 0, NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbl_adopter`
--
ALTER TABLE `tbl_adopter`
  ADD PRIMARY KEY (`adopter_id`),
  ADD UNIQUE KEY `adopter_username` (`adopter_username`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `idx_adopter_archived` (`archived`);

--
-- Indexes for table `tbl_adoption`
--
ALTER TABLE `tbl_adoption`
  ADD PRIMARY KEY (`adoption_id`),
  ADD UNIQUE KEY `uk_pet_adoption` (`pet_id`),
  ADD KEY `fk_adoption_pet` (`pet_id`),
  ADD KEY `fk_adoption_adopter` (`adopter_id`),
  ADD KEY `fk_adoption_user` (`user_id`),
  ADD KEY `idx_adoption_date` (`adoption_date`),
  ADD KEY `idx_adoption_archived` (`archived`);

--
-- Indexes for table `tbl_adoption_request`
--
ALTER TABLE `tbl_adoption_request`
  ADD PRIMARY KEY (`adoption_request_id`),
  ADD UNIQUE KEY `uk_adopter_pet_request` (`adopter_id`,`pet_id`,`status`),
  ADD KEY `fk_request_pet` (`pet_id`),
  ADD KEY `fk_request_adopter` (`adopter_id`),
  ADD KEY `fk_request_user` (`user_id`),
  ADD KEY `idx_request_status` (`status`),
  ADD KEY `idx_request_date` (`request_date`),
  ADD KEY `idx_adoption_request_archived` (`archived`);

--
-- Indexes for table `tbl_pet`
--
ALTER TABLE `tbl_pet`
  ADD PRIMARY KEY (`pet_id`),
  ADD UNIQUE KEY `pet_id` (`pet_id`),
  ADD KEY `fk_pet_owner` (`pet_owner_id`),
  ADD KEY `fk_pet_type` (`pet_type_id`),
  ADD KEY `idx_pet_adoption_status` (`adoption_status`),
  ADD KEY `idx_pet_type` (`pet_type_id`),
  ADD KEY `idx_pet_owner` (`pet_owner_id`),
  ADD KEY `idx_pet_archived` (`archived`);

--
-- Indexes for table `tbl_pet_media`
--
ALTER TABLE `tbl_pet_media`
  ADD PRIMARY KEY (`pet_media_id`),
  ADD KEY `fk_pet_media` (`pet_id`);

--
-- Indexes for table `tbl_pet_owner`
--
ALTER TABLE `tbl_pet_owner`
  ADD PRIMARY KEY (`pet_owner_id`),
  ADD UNIQUE KEY `pet_owner_id` (`pet_owner_id`),
  ADD UNIQUE KEY `pet_owner_username` (`pet_owner_username`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `idx_pet_owner_archived` (`archived`);

--
-- Indexes for table `tbl_pet_type`
--
ALTER TABLE `tbl_pet_type`
  ADD PRIMARY KEY (`pet_type_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_user_role` (`role`),
  ADD KEY `idx_users_archived` (`archived`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbl_adopter`
--
ALTER TABLE `tbl_adopter`
  MODIFY `adopter_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_adoption`
--
ALTER TABLE `tbl_adoption`
  MODIFY `adoption_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tbl_adoption_request`
--
ALTER TABLE `tbl_adoption_request`
  MODIFY `adoption_request_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_pet`
--
ALTER TABLE `tbl_pet`
  MODIFY `pet_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_pet_media`
--
ALTER TABLE `tbl_pet_media`
  MODIFY `pet_media_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tbl_pet_owner`
--
ALTER TABLE `tbl_pet_owner`
  MODIFY `pet_owner_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `tbl_pet_type`
--
ALTER TABLE `tbl_pet_type`
  MODIFY `pet_type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tbl_adopter`
--
ALTER TABLE `tbl_adopter`
  ADD CONSTRAINT `fk_adopter_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `tbl_adoption`
--
ALTER TABLE `tbl_adoption`
  ADD CONSTRAINT `fk_adoption_adopter` FOREIGN KEY (`adopter_id`) REFERENCES `tbl_adopter` (`adopter_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_adoption_pet` FOREIGN KEY (`pet_id`) REFERENCES `tbl_pet` (`pet_id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_adoption_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `tbl_adoption_request`
--
ALTER TABLE `tbl_adoption_request`
  ADD CONSTRAINT `fk_request_adopter` FOREIGN KEY (`adopter_id`) REFERENCES `tbl_adopter` (`adopter_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_request_pet` FOREIGN KEY (`pet_id`) REFERENCES `tbl_pet` (`pet_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_request_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `tbl_pet`
--
ALTER TABLE `tbl_pet`
  ADD CONSTRAINT `fk_pet_owner` FOREIGN KEY (`pet_owner_id`) REFERENCES `tbl_pet_owner` (`pet_owner_id`),
  ADD CONSTRAINT `fk_pet_type` FOREIGN KEY (`pet_type_id`) REFERENCES `tbl_pet_type` (`pet_type_id`);

--
-- Constraints for table `tbl_pet_media`
--
ALTER TABLE `tbl_pet_media`
  ADD CONSTRAINT `fk_pet_media` FOREIGN KEY (`pet_id`) REFERENCES `tbl_pet` (`pet_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `tbl_pet_owner`
--
ALTER TABLE `tbl_pet_owner`
  ADD CONSTRAINT `fk_pet_owner_user` FOREIGN KEY (`username`) REFERENCES `users` (`username`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
