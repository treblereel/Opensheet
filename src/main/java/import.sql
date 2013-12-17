SET foreign_key_checks = 0;

--
-- Dumping data for table `values`
--
INSERT INTO `values` (`id`, `type`, `key`, `value`) VALUES (1, '', 'timeSheetInputMode', 'true&2012-03-31');


--
-- Dumping data for table `branch`
--



INSERT INTO `branch` (`id`, `name`, `owner_id`, `status`, `branch_index`) VALUES (1, 'default', 80, 1, 'default');



--
-- Dumping data for table `authmethod`
--

INSERT INTO `authmethod` (`id`, `type`, `data`, `description`, `scannable`) VALUES (1, 'db', '', 'Internal database', 0);

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`id`, `branch_id`, `name`, `owner`, `status`, `started`, `finished`, `updated`, `note`) VALUES (1, 2, 'default', 1, 1, '2012-01-01', '2012-01-01', '2012-01-01', 'empty');


--
-- Dumping data for table `REVINFO`
--

INSERT INTO `REVINFO` (`REV`, `REVTSTMP`) VALUES (1, 1328048817790);

--
-- Dumping data for table `person_rate`
--

INSERT INTO `person_rate` (`id`, `date`, `internal_rate`) VALUES (1, '2012-02-01', 0);


--
-- Dumping data for table `person_rate_AUD`
--

INSERT INTO `person_rate_AUD` (`id`, `REV`, `REVTYPE`, `date`, `internal_rate`) VALUES (1, 1, 0,'2012-02-01', 0);



--
-- Dumping data for table `person`
--

INSERT INTO `person` (`id`, `login`, `password`, `first_name`, `second_name`, `status`, `started`, `finished`, `updated`, `email`,`lang`, `note`, `department`, `branch`, `authmethod`, `permission_id`, `rate_id`) VALUES (1, 'admin', '14351d190b079579ccc78bdaecd0eb63',' admin', 'sys', 1, '2012-01-01', '2012-01-01', '2012-01-01', 'test.com','en', NULL, 1, 1, 1, 1, 1);




--
-- Dumping data for table `permission`
--

INSERT INTO `permission` (`admin`, `fd`, `dm`, `pm`, `id`, `user_id`) VALUES (1, 0, 0, 0, 1, 1);



SET foreign_key_checks = 1;