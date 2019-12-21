package asa.org.bd.ammsma.database;



class CreateTableScripts {



    String createTempInsurance() {
        return "CREATE TABLE InsuranceTemp (id integer primary key AUTOINCREMENT NOT NULL, Amount int not null, WorkingDay int)";
    }



    String createTransaction() {
        return "CREATE TABLE [P_Transaction](	[Id] integer primary key AUTOINCREMENT NOT NULL, Status text, [P_AccountId] [int] NOT NULL,	[Type] [int]  NULL,	[Date] [int] NOT NULL, Balance int, TransactionStatus int not null, ProgramOfficerID int, [Debit] [float] NOT NULL,	[Credit] [float] NOT NULL,	[Process] [int] NULL, Program_Type int,	Tag text, [ChequeNumber] [varchar](50)  NULL, programID int,	[IsAcPayable] [int] NULL DEFAULT ((0))) ";
    }


    String createTransactionHistory() {
        return "CREATE TABLE [P_TransactionHistory]([Id] integer primary key AUTOINCREMENT NOT NULL,[P_AccountID] [int] NOT NULL, [Type] int not null, [Process] [int] NOT NULL,  [Date] [int] NOT NULL, [Amount] [float] not null)";
    }


    String createLoanRecord() {
        return "CREATE TABLE P_LoanRecord(Id integer primary key AUTOINCREMENT NOT NULL,P_MemberID int not null,  ProgramID int,Duration int, InstallmentTypeID int, DisbursedDate int, PrincipalAmount long, FundTypeID int, SchemeID int, LoanInsurance float, IntentID int)";
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////

    String createAdmin() {
        return "CREATE TABLE Admin(Id INTEGER PRIMARY KEY, Name TEXT, Pass TEXT);";
    }

    String createDays() {
        return "CREATE TABLE Days (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, DayID INTEGER NOT NULL, Day TEXT NOT NULL, ShortName TEXT NULL)";
    }

    String createInstallmentType() {
        return "CREATE TABLE [P_InstallmentType]([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[Installment_Type] [INTEGER] NOT NULL,[Name] [VARCHAR](100)  NULL)";
    }

    String createGroupType() {
        return "CREATE TABLE P_GroupType(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Name TEXT NOT NULL);";
    }

    String createType() {
        return "CREATE TABLE [P_Type]([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[Type] [INTEGER] NOT NULL,[TypeName] [VARCHAR](100)  NOT NULL)";
    }

    String createProcess() {
        return "CREATE TABLE [P_Process]([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[Process] [INTEGER] NOT NULL,[ProcessName] [VARCHAR](100)  NOT NULL)";
    }



    String createAccount() {
        return "CREATE TABLE [P_Account]([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [Account_ID] INTEGER NOT NULL, [P_ReferenceProgramId] [INTEGER] NULL,[P_MemberId] [INTEGER] NOT NULL,"
                + "[P_ProgramId] [INTEGER] ,[P_ProgramTypeId] [INTEGER] ,[P_Duration] [INTEGER] ,[ProgramName] [String] NULL, [P_InstallmentType] [INTEGER] ,"
                + "[IsSupplementary] [BOOLEAN],	[Cycle] [INTEGER] NULL,	[OpeningDate] [INTEGER] NULL,	[MemberSex] [INTEGER] NULL,	[DisbursedAmount] [DOUBLE], [ServiceChargeAmount] [FLOAT] ,"
                + "	[MinimumDeposit] [FLOAT] ,	Flag [INT] NOT NULL, ProgramOfficerID INTEGER, [MeetingDayOfWeek] [INTEGER] NULL,	[MeetingDayOfMonth] [INTEGER] NULL,	[FirstInstallmentDate] [INTEGER] NULL,"
                + "	[ReceiveDate] [INTEGER] NULL,	[ReceiveAmount] [DOUBLE] NULL,	[Status] [INTEGER],  [GracePeriod] [INTEGER] NULL,"
                + " [P_SchemeId] [INTEGER] NULL, [P_FundId] [INTEGER] NULL, [DisbursedAmountWithSC] [FLOAT] NULL, [LoanInsurance] [FLOAT] NULL, [NewLoan] [INT] NULL, [NewAccount] [INT] NULL )";
    }

    String createAccountBalance() {
        return "CREATE TABLE [P_AccountBalance] ([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, P_AccountId INTEGER NOT NULL, [Date] [INTEGER] NULL,[Debit] [FLOAT] ,[Credit] [FLOAT],[ProgramType] [INTEGER] NOT NULL, [Type] [INT] NOT NULL,[CreateDateTime] [STRING] NULL,[Flag] [INT] NULL, ProgramOfficerID INT NULL , Status STRING NULL, StatusLoan STRING NULL )";
    }

    String createAccountDetails(){
        return "CREATE TABLE [P_AccountDetails]( [ID] INTEGER primary key AUTOINCREMENT NOT NULL, P_AccountId INTEGER NOT NULL, P_LoanTransactionDate INTEGER NOT NULL, Type INTEGER NOT NULL, Amount FLOAT NOT NULL," +
                " Process int NOT NULL, NewlyCreated Boolean NULL)";
    }
    String createBranch() {
        return "CREATE TABLE Branch(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, BranchID INTEGER NOT NULL, Name TEXT, OpeningDay TEXT, Mobile TEXT, AMMSVersion TEXT, DistrictId INTEGER, BranchType INTEGER NOT NULL);";
    }

    String createCalender() {
        return "CREATE TABLE Calender(Calender_Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Id INT  NOT NULL, Date INTEGER NOT NULL, RealDate STRING NOT NULL, IsWeeklyHoliday BOOLEAN NOT NULL, IsSpecialHoliday BOOLEAN NOT NULL, Description TEXT NULL, OpenORClose STRING NOT NULL, Day INTEGER NOT NULL, Month INTEGER NOT NULL, Year INTEGER NOT NULL, DayShortName STRING NOT NULL, DayId INTEGER NOT NULL )";
    }

    String createDuration() {
        return "CREATE TABLE P_Duration( [Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, [Duration] INTEGER NOT NULL)";
    }



    String createGracePeriod()
    {
        return "CREATE TABLE P_GracePeriod (Id INTEGER PRIMARY KEY NOT NULL, P_ProgramId INTEGER NOT NULL, P_Duration INTEGER NOT NULL," +
                " P_InstallmentType INTEGER NOT NULL, Sex INTEGER NOT NULL, GracePeriod INTEGER NOT NULL, StartingDate INTEGER NOT NULL, EndingDate INTEGER NOT NULL)";
    }



    String createGroup() {
        return "CREATE TABLE [P_Group](Prim_Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[P_ProgramOfficerId] [INTEGER] NOT NULL, P_GroupTypeId INTEGER NOT NULL, [P_DefaultProgramId] [INTEGER] NOT NULL,[Name] [VARCHAR](100)  NOT NULL,[FormationDate] [DATETIME]," +
                "[Village] [VARCHAR](100)  NOT NULL,[MeetingDay] [INTEGER] NOT NULL,[Status] [INTEGER] NOT NULL,[ClosingDate] [DATETIME] NULL,[MinimumSavingsDeposit] [INTEGER] NOT NULL,[MinimumSecurityDeposit] [INTEGER] NOT NULL,[GroupLeaderName] [VARCHAR](100)  NULL," +
                "[GroupLeaderAddress] [VARCHAR](100)  NULL,[GroupLeaderPhone] [VARCHAR](100)  NULL,FullName [VARCHAR] NOT NULL,Flag [INT] NULL, ID INTEGER NULL)";

    }

    String createInstallmentAmount()
    {
        return "CREATE TABLE P_InstallmentAmount (Id INTEGER PRIMARY KEY NOT NULL, P_ProgramId INTEGER NOT NULL, P_Duration INTEGER NOT NULL, " +
                "P_InstallmentType INTEGER NOT NULL, GracePeriod INTEGER NOT NULL, Sex INTEGER NOT NULL, CalculationMode INTEGER NOT NULL, BaseAmount FLOAT NOT NULL, " +
                " AmountPerBase FLOAT NOT NULL, StartingDate INTEGER NOT NULL, EndingDate INTEGER NOT NULL)";
    }

    String createInstallmentCount()
    {
        return "CREATE TABLE P_InstallmentCount (Id INTEGER PRIMARY KEY NOT NULL, P_ProgramId INTEGER NOT NULL, P_Duration INTEGER NOT NULL, " +
                    "P_InstallmentType INTEGER NOT NULL, GracePeriod INTEGER NOT NULL, Sex INTEGER NOT NULL, InstallmentCount INTEGER NOT NULL," +
                    " StartingDate INTEGER NOT NULL, EndingDate INTEGER NOT NULL)";
    }

    String createLoanGroup()
    {
        return "CREATE TABLE P_LoanGroup (Id INTEGER NOT NULL, P_GroupTypeId INTEGER NOT NULL, P_LoanGroupProgramId INTEGER NOT NULL, P_ProgramId INTEGER NOT NULL," +
                " P_DefaultInstallmentType INTEGER NOT NULL, P_DefaultDuration INTEGER NOT NULL, DefaultSex INTEGER NOT NULL, IsSupplementary BOOLEAN NOT NULL)";
    }

    String createLoanGroupDuration()
    {
        return "CREATE TABLE P_LoanGroupDuration(Id INTEGER PRIMARY KEY NOT NULL, P_GroupTypeId INTEGER NOT NULL, P_LoanGroupProgramId INTEGER NOT NULL, P_ProgramId INTEGER NOT NULL, P_Duration INTEGER NOT NULL, StartingDate  DATETIME NOT NULL, EndingDate DATETIME NOT NULL," +
                " StartingDateDuration INTEGER NULL, EndingDateDuration INTEGER NULL, SortOrder INTEGER NOT NULL)";
    }

    String createLoanGroupInstallment()
    {
        return "CREATE TABLE P_LoanGroupInstallment( PrimaryKey INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Id INTEGER  NOT NULL, P_GroupTypeId INTEGER NOT NULL, P_LoanGroupProgramId INTEGER NOT NULL, P_ProgramId INTEGER NOT NULL, P_Duration INTEGER NOT NULL, P_InstallmentType INTEGER NOT NULL, StartingDate  DATETIME NOT NULL, EndingDate DATETIME NOT NULL," +
                    " StartingDateDuration INTEGER NULL, EndingDateDuration INTEGER NULL, SortOrder INTEGER NOT NULL)";
    }
    String createLoanTransaction()
    {
        return "CREATE TABLE P_LoanTransaction (Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, P_AccountId INTEGER NOT NULL, Date INTEGER NOT NULL, Debit FLOAT NOT NULL, Status BOOLEAN NOT NULL, Flag [INT] NULL)";
    }

    String createMemberView() {
        return "CREATE TABLE P_MemberView ( PrimaryKey INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Id INTEGER NOT NULL, P_GroupId INTEGER NOT NULL, P_ProgramId INTEGER NOT NULL, PassbookNumber INTEGER NOT NULL, Name VARCHAR(100) NOT NULL, FatherOrHusbandName VARCHAR(100) NOT NULL, IsHusband BOOLEAN NOT NULL, DateOfBirth STRING NOT NULL,"
                + " Status INTEGER NOT NULL, AdmissionDate INT NOT NULL, NationalIdNumber VARCHAR(50) NULL, Sex INTEGER NOT NULL, Phone VARCHAR(25) NULL , NewStatus TEXT NOT NULL, ReceiveDate INTEGER  NULL, ReceiveType INTEGER  NULL, UpdateNid  INTEGER NULL, UpdatePhone  INTEGER NULL, BirthCertificateNumber VARCHAR(50) NULL, IsWithoutLoan BOOLEAN NOT NULL)";
    }

    String createMemberNew() {
        return "CREATE TABLE P_MemberNew ( PrimaryKey INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Id INTEGER NOT NULL, P_GroupId INTEGER NOT NULL, P_ProgramId INTEGER NOT NULL, PassbookNumber INTEGER NOT NULL, Name VARCHAR(100) NOT NULL, FatherOrHusbandName VARCHAR(100) NOT NULL, IsHusband BOOLEAN NOT NULL, DateOfBirth STRING NOT NULL,"
                + " Status INTEGER NOT NULL, AdmissionDate INT NOT NULL, AdmissionDateString STRING NOT NULL, NationalIdNumber VARCHAR(50) NULL, Sex INTEGER NOT NULL, Phone VARCHAR(25) NULL , NewStatus TEXT NOT NULL, MemberNickName VARCHAR(50) NULL, MemberFullName VARCHAR(50) NULL, FatherNickName VARCHAR(50) NULL, FatherFullName VARCHAR(50) NULL, MotherName VARCHAR(50) NULL, EducationInfo TEXT NULL, ProfessionInfo TEXT NULL, "
                + " MemberAge INT NULL, Nationality INT NULL, ReligionInfo INT NULL, Ethnicity INT NULL, MaritalStatus INT NULL , SpouseFullName VARCHAR(50) NULL, "
                + " SpouseNickName VARCHAR(50) NULL, GuardianName VARCHAR(50) NULL, GuardianRelation VARCHAR(50) NULL, BirthCertificateNumber VARCHAR(50) NULL, ResidenceType VARCHAR(50) NULL, LandLordName VARCHAR(50) NULL, "
                + " PermanentDistrictId INT NULL, PermanentUpazila VARCHAR(50) NULL, PermanentUnion VARCHAR(50) NULL, PermanentPostOffice VARCHAR(50) NULL, PermanentVillage VARCHAR(50) NULL, PermanentRoad VARCHAR(50) NULL, PermanentHouse VARCHAR(50) NULL,  PermanentFixedProperty TEXT NULL, PermanentIntroducerName VARCHAR(50) NULL, PermanentIntroducerDesignation VARCHAR(50) NULL,"
                + "   PresentDistrictId INT NULL, PresentUpazila VARCHAR(50) NULL, PresentUnion VARCHAR(50) NULL, PresentPostOffice VARCHAR(50) NULL, PresentVillage VARCHAR(50) NULL, PresentRoad VARCHAR(50) NULL, PresentHouse VARCHAR(50) NULL, PresentPhone  VARCHAR(20) NULL , PresentPermanentSame BOOLEAN NULL, SavingsDeposit INT NULL, SecurityDeposit INT NULL, ReceiveDate INTEGER  NULL, ReceiveType INTEGER  NULL )";
    }

    String createOtherFee(){
        return "CREATE TABLE P_OtherFee ( Id INTEGER PRIMARY KEY NOT NULL, P_ProgramId INTEGER NOT NULL, P_Duration INTEGER NOT NULL, P_InstallmentType INTEGER NOT NULL, Sex INTEGER NOT NULL, ShortName VARCHAR(100) NOT NULL, Name VARCHAR(100) NOT NULL," +
                " Amount DOUBLE NOT NULL, TransactionType INTEGER NOT NULL, IsMandatory BOOLEAN NOT NULL, WhileDisbursing BOOLEAN NOT NULL, IsFixed BOOLEAN NOT NULL," +
                " IsExemptable BOOLEAN NOT NULL, StartingDate DATETIME NOT NULL, EndingDate DATETIME NOT NULL, SortOrder INTEGER NOT NULL)";
    }

    String createProgram() {
        return "CREATE TABLE P_Program ( Id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, P_ProgramTypeId INTEGER NOT NULL, ShortName VARCHAR (100) NOT NULL,	Name VARCHAR(100) NULL,	Description VARCHAR(300), IsPrimary BOOLEAN NOT NULL," +
                " IsLongTerm BOOLEAN NOT NULL, IsCollectionSheet BOOLEAN NOT NULL, StartingDate INTEGER NOT NULL, EndingDate INTEGER NOT NULL, SortOrder INTEGER NOT NULL, Program_ID INTEGER NOT NULL)";
    }

    String createProgramGroupType()
    {
        return "CREATE TABLE P_ProgramGroupType (Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, P_GroupTypeId INTEGER NOT NULL, P_ProgramId INTEGER NOT NULL)";
    }


    String createProgramOfficer() {
        return "CREATE TABLE P_ProgramOfficer ( Id INTEGER PRIMARY KEY  NOT NULL, Code INTEGER NOT NULL, Name STRING NOT NULL, Designation STRING NOT NULL, StartingDate DATETIME NOT NULL, EndingDate DATETIME NULL, Status INTEGER NOT NULL )";
    }

    String createSchedule() {
        return "CREATE TABLE [P_Schedule]([Id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[P_AccountId] [INTEGER] NOT NULL,[MaxInstallmentNumber] [INTEGER] NOT NULL,[BaseInstallmentAmount] [FLOAT] NOT NULL,[InstallmentAmount] [FLOAT] NOT NULL,[ScheduledDate] [INTEGER] NOT NULL,"
                + "[Scheduled] [BOOLEAN] NOT NULL,[NextDate] [INTEGER] NOT NULL, [PaidAmount] [FLOAT] NULL,[AdvanceAmount] [FLOAT] NULL, [OverdueAmount] [FLOAT] NOT NULL,[OutstandingAmount] [FLOAT] NOT NULL, [PrincipalOutstanding] [FLOAT] NOT NULL DEFAULT ((0)),"
                + "	ScheduleID TEXT)";
    }

    String createServiceCharge()
    {
        return "CREATE TABLE P_ServiceCharge (Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, P_ProgramId INTEGER NOT NULL, P_Duration INTEGER NOT NULL, P_InstallmentType INTEGER NOT NULL, Sex INTEGER NOT NULL," +
                " ServiceCharge DOUBLE NOT NULL, StartingDate STRING NOT NULL, EndingDate STRING NOT NULL, StartingDateInteger INTEGER NOT NULL, EndingDateInteger INTEGER NOT NULL, DecliningServiceCharge DOUBLE NOT NULL, P_FundId INTEGER NULL)";
    }


    String createUser() {
        return "CREATE TABLE User (Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Login NVARCHAR(10) NOT NULL, Password NVARCHAR(10) NOT NULL, Name NVARCHAR(100) NOT NULL, IsActive BOOLEAN NOT NULL, ProgramOfficerId INTEGER)";
    }

    /*String createTempMemberGroupCollection() {
        return "CREATE TABLE MemberGroupCollection (Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, GroupId INTEGER NOT NULL, MemberID INTEGER NOT NULL, PaidOrNot BOOLEAN NOT NULL, RealizedOrNot BOOLEAN NOT NULL, OverDueOrNot BOOLEAN NOT NULL, TermOverDue BOOLEAN NOT NULL)";
    }*/


    String createFund() {
        return "CREATE TABLE [P_Fund] ([Id] INTEGER PRIMARY KEY,[Name] [VARCHAR](100)  NULL)";
    }

    /*String insertFund() {
        return "INSERT INTO [P_Fund] ([Id],[Name]) VALUES (24 ,'AB'), (28 ,'AB-ADB'), (32 ,'AB-ASA'), (31 ,'AB-Citi'), (34 ,'AB-PT-AGENT'), (33 ,'AB-PT-USER'), (30 ,'AB-SCB'), (25 ,'EDU'), (26 ,'EME'), (37 ,'HA'), (36 ,'IL'), (-1 ,'OTHER')"
                + ", (29 ,'SAN'), (22 ,'SB'), (23 ,'SEL'), (35 ,'SF'), (21 ,'SL'), (39 ,'SMAP-ASA'), (38 ,'SMAP-BB'), (27 ,'SOLAR')";
    }*/



    String createScheme() {
        return "CREATE TABLE [P_Scheme] ([Id] INTEGER PRIMARY KEY,[Name] [varchar](100)  NULL, P_SchemeCategoriesId INTEGER NOT NULL)";
    }


    /*String insertScheme() {
        return  "INSERT INTO [P_Scheme] ([Id],[Name], [P_SchemeCategoriesId]) VALUES (1 ,'OTHERS' ,39),(2 ,'ADMISSION FEE' ,12),(3 ,'AGRI (CROPS)' ,30),"
                + " (5 ,'AGRI (VEGETABLES)' ,30), "
                + " (6 ,'AGRI EQUIPMENT (TRACTOR, POWER' ,30), "
                + " (7 ,'AGRI FRUITS' ,31), "
                + "  (8 ,'AGRI PROCESSING & MARKETING' ,8), "
                + "  (9 ,'AUTO RICKSHAW' ,46), "
                + "  (10 ,'BAMBOO AND CANE WORK' ,19), "
                + "  (12 ,'BISCUIT BAKERIES' ,27) "
                + ",(13 ,'BOAT BUYING' ,38) "
                + ",(14 ,'BOOK BUYING' ,12) "
                + ",(15 ,'BOOK LIBRARY' ,42) "
                + ",(16 ,'BUS' ,46) "
                + ",(17 ,'CATTLE FATTENING' ,5) "
                + ",(18 ,'CLOTH SALE' ,42) "
                + ",(19 ,'CLOTHES SHOPS' ,42)"
                + " ,(20 ,'COMPUTER PURCHASE' ,9) "
                + ",(21 ,'COMPUTER TRAINING CENTER' ,27) "
                + ",(22 ,'CONFECTIONERY' ,42) "
                + ",(23 ,'COTTAGE INDUSTRIES' ,27) "
                + ",(25 ,'COW REARING' ,10) "
                + ",(26 ,'DAIRY FARM' ,10) "
                + ",(27 ,'DIAGNOSTIC CENTER' ,27) "
                + ",(28 ,'ENGINEERING WORKSHOP' ,27) "
                + ",(29 ,'FERTILIZER & INSECTICIDE SHOP' ,42) "
                + ",(30 ,'FAST FOOD' ,42) "
                + ",(31 ,'FISH FARMING' ,14) "
                + ",(32 ,'FISHERIES' ,14) "
                + ",(33 ,'FORM FILL-UP' ,12) "
                + ",(34 ,'FOWL FARMING' ,11) "
                + ",(35 ,'FURNITURE MAKING' ,42) "
                + ",(36 ,'GARMENTS FACTORY' ,42) "
                + ",(37 ,'GOAT FARMING' ,15) "
                + ",(38 ,'GOAT REARING' ,15) "
                + ",(40 ,'GROCERY SHOPS' ,42)"
                + ", (42 ,'HANDICRAFTS' ,19)"
                + ", (43 ,'HAWKERING' ,42)"
                + ",(44 ,'HOTEL' ,42)"
                + ",(45 ,'IRRIGATION (POWER PUMP, SHALLO' ,30)"
                + ",(46 ,'JUTE WORK' ,19)"
                + ",(48 ,'LAUNDRY SHOP' ,42)"
                + ",(49 ,'LEATHER WORKSHOP' ,42)"
                + ",(50 ,'NET MAKING' ,19)"
                + ",(51 ,'MUSHROOM CULTIVATION' ,28)"
                + ",(52 ,'NURSERY' ,31)"
                + ",(53 ,'OIL MILL' ,42)"
                + ",(55 ,'OLD CLOTHES SELLING' ,42)"
                + ",(56 ,'PHARMACY SHOP' ,42)"
                + ",(57 ,'PHONE AND FAX SHOP' ,42)"
                + ",(58 ,'PLASTIC MANUFACTURE' ,27)"
                + ",(59 ,'POULTRY FARM' ,11)"
                + ",(60 ,'PRINTING PRESS' ,27)"
                + ",(61 ,'REGISTRATION FEE' ,12)"
                + ",(62 ,'RESTAURANT' ,42)"
                + ",(63 ,'RICE HUSKING' ,16)"
                + ",(64 ,'RICE MILL & BUSINESS' ,27)"
                + ",(65 ,'RICKSHAW BUYING' ,19)"
                + ",(66 ,'RICKSHAW GARAGE' ,42)"
                + ",(67 ,'SALOON  SHOP' ,42)"
                + ",(68 ,'SEASONAL CROPS' ,30)"
                + ",(69 ,'SEASONAL FRUIT' ,30)"
                + ",(70 ,'SEASONAL VEGETABLES' ,30)"
                + ",(71 ,'SEED SALE' ,42)"
                + ",(72 ,'SELLING SHAREE' ,42)"
                + ",(73 ,'SHOE FACTORY' ,42)"
                + ",(74 ,'STATIONERIES SHOPS' ,42)"
                + ",(75 ,'STOCK BUSINESS' ,42)"
                + ",(76 ,'TAILORING SHOP' ,42)"
                + ",(77 ,'TEA STALL' ,42)"
                + ",(78 ,'TOY FACTORY' ,42)"
                + ",(79 ,'TRADING' ,42)"
                + ",(80 ,'VAN BUYING' ,38)"
                + ",(81 ,'VEGETABLE CULTIVATION' ,30)"
                + ",(83 ,'VEGETABLES SELLING' ,19)"
                + ",(84 ,'WEAVING' ,19)"
                + ",(85 ,'BOAT BUILDING' ,19)"
                + ",(86 ,'FLOWER PLANTATION' ,32)"
                + ",(87 ,'HOUSING' ,18)"
                + ",(88 ,'POTATO CULTIVATION' ,35)"
                + ",(89 ,'AMAN PADDY' ,37)"
                + ",(90 ,'BORO PADDY' ,4)"
                + ",(91 ,'WHEAT CULTIVATION' ,47)"
                + ",(92 ,'SUGARCANE CULTIVATION' ,43)"
                + ",(93 ,'MUSTERED/PEANUT' ,29)"
                + ",(94 ,'VEGETABLES CULTIVATION (SUMMER' ,44)"
                + ",(95 ,'AUSH PADDY' ,2)"
                + ",(96 ,'JUTE CULTIVATION' ,25)"
                + ",(97 ,'CORN' ,26)"
                + ",(98 ,'VEGETABLES CULTIVATION (WINTER' ,48)"
                + ",(99 ,'COTTON' ,6)"
                + ",(100 ,'OTHERS (CROP)',32)"
                + ",(101 ,'SHRIMP CULTIVATION' ,40)"
                + ",(102 ,'AQUA CULTURE' ,1)"
                + ",(103 ,'HATCHERY' ,17)"
                + ",(104 ,'SALT CULTIVATION' ,41)"
                + ",(105 ,'BANANA' ,34)"
                + ",(106 ,'STORAGE & MARKETING OF CROP' ,8)"
                + ",(107 ,'DEEP TUBEWELL' ,21)"
                + ",(108 ,'SHALLOW TUBEWELL' ,23)"
                + ",(109 ,'L.L.P.' ,22)"
                + ",(110 ,'POWER PUMP' ,20)"
                + ",(111 ,'COW FOR CULTIVATION' ,7)"
                + ",(112 ,'COW FATTENING' ,5)"
                + ",(113 ,'GOAT/SHEEP FARM (POULTRY)',15)"
                + ",(114 ,'POWER TILLER' ,36)"
                + ",(115 ,'TRACTOR' ,45)"
                + ",(116 ,'HARVESTOR' ,16)"
                + ",(117 ,'OTHER AGRI-EQUIPPING' ,33)"
                + ",(118 ,'BETEL LEAF BOROZ' ,3)"
                + ",(119 ,'INCOME GENERATING ACTIVITY' ,19)"
                + ",(120 ,'RURAL TRANSPORT' ,38)"
                + ",(121 ,'JOLMOHAL MANAGEMENT' ,24)"
                + ",(122 ,'OTHER SEASONAL ACTIVITY (SERIC' ,39)"
                + ",(123 ,'ROWER PUMP' ,20)"
                + ",(124 ,'BANANA CULTIVATION' ,34)"
                + ",(125 ,'BROILER CHICKEN FARMING' ,11)"
                + ",(126 ,'CNG VEHICLE' ,46)"
                + ",(127 ,'CORN CULTIVATION' ,26)"
                + ",(128 ,'COTTON INDUSTRIES' ,27)"
                + ",(129 ,'EDUCATION LOAN' ,12)"
                + ",(130 ,'EMERGENCY LOAN' ,13)"
                + ",(131 ,'ENGINE BOAT/TRAWLER' ,38)"
                + ",(132 ,'FAST FOOD & CONFECTIONERY SHOP' ,42)"
                + ",(133 ,'FISH CULTURE' ,14)"
                + ",(134 ,'FURNITURE SHOP' ,42)"
                + ",(135 ,'GENERATOR' ,9)"
                + ",(136 ,'HAND MAKING SHOWPIECE' ,19)"
                + ",(137 ,'HATCHLING FARM' ,11)"
                + ",(138 ,'HOTEL & RESTAURENT' ,42)"
                + ",(139 ,'HOUSING LOAN' ,18)"
                + ",(140 ,'LAYER CHICKEN FARMING' ,11)"
                + ",(141 ,'LEATHER WORK' ,42)"
                + ",(142 ,'METAL WORKS' ,42)"
                + ",(143 ,'MICRO BUS' ,9)"
                + ",(144 ,'MOTOR CYCLE' ,9)"
                + ",(145 ,'OLD CLOTH SHOP' ,42)"
                + ",(146 ,'OTHER HANDCRAFTS' ,19)"
                + ",(147 ,'PRAWN HATCHERY' ,40)"
                + ",(148 ,'PRAWN NURSERY' ,40)"
                + ",(149 ,'RASHOM CULTURE' ,39)"
                + ",(150 ,'SHALLOW MACHINE' ,23)"
                + ",(151 ,'SOLAR LIGHT' ,9)"
                + ",(152 ,'SWAN FARM' ,11)"
                + ",(153 ,'TRANSPORTATION' ,38)"
                + ",(154 ,'TREE NURSERY' ,31)"
                + ",(155 ,'VEGETABLE SHOP' ,42)"
                + ",(156 ,'CROP CULTIVATION' ,32)"
                + ",(157 ,'AGRICULTURAL EQUIPMENT' ,33)"
                + ",(158 ,'LIVESTOCK AND FISHERIES' ,14)";
    }*/




    String createProgramNameChange() {
        return "CREATE TABLE [ProgramNameChange] ([P_KEY] INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL,[Id] INTEGER  NOT NULL,[ShortName] STRING NOT NULL, [ChangedName] STRING NOT NULL, [ValidName] STRING NOT NULL)";
    }


    String insertProgramNameChange() {
        return  "INSERT INTO [ProgramNameChange] ([Id],[ShortName], [ChangedName], [ValidName]) VALUES "
                + "(106, 'EDUCATION LOAN','EDUCATION LOAN','Education Loan'), "
                + "(107, 'BUSINESS DEVELOPMENT SERVICE LOAN','BDS LOAN','Business Development Service Loan'),"
                + "(108, 'REHABILITATION LOAN','REHAB LOAN','Rehabilitation Loan'),"
                + "(120, 'SOLAR LANTERN','SOLAR LAN LOAN','Solar Lantern'),"
                + "(121, 'SOLAR HOME SYSTEM','SOLAR HOME LOAN','Solar Home System'),"
                + "(122, 'SIX SEATED TRY CYCLE','SIX STC LOAN','Six Seated Try cycle'),"
                + "(123, 'TRI CYCLE-SOLAR','SOLAR TC LOAN','Tri Cycle-Solar'),"
                + "(124, 'SOLAR WATER PUMP','SOLAR WP LOAN','Solar Water Pump'),"
                + "(129, 'PRIMARY LOAN','PRIMARY LOAN','Primary Loan'),"
                + "(130, 'SPECIAL LOAN','SPECIAL LOAN','Special Loan'),"
                + "(131, 'SANITATION ENTREPRENEUR LOAN','SAN-ENT LOAN','Sanitation Entrepreneur Loan'),"
                + "(132, 'SANITATION USER','SAN-USER LOAN','Sanitation User'),"
                + "(135, 'SAVINGS FRIENDLY LOAN','SAVINGS FND LOAN','Savings Friendly Loan'),"
                + "(137, 'SMAP LOAN','SMAP LOAN','SMAP Loan'),"
                + "(138, 'CROSSBREED MILKING COW LOAN','CBMC LOAN','Crossbreed Milking Cow Loan'),"
                + "(140, 'SANITATION LOAN-WB','SAN-WB LOAN','Sanitation Loan-WB'),"
                + "(141, 'MICRO ENTREPRENEUR LOAN','MICRO-ENT LOAN','Micro Entrepreneur Loan'),"
                + "(142, 'SMALL ENTREPRENEUR LOAN','SMALL-ENT LOAN','Small Entrepreneur Loan'),"
                + "(143, 'MEDIUM ENTREPRENEUR LOAN','MEDIUM-ENT LOAN','Medium Entrepreneur Loan'),"
                + "(144, 'MSME SHORT TERM LOAN','MSME-ST LOAN','MSME Short Term Loan'),"
                + "(204, 'LONG TERM SAVINGS','LTS','Long Term Savings'),"
                + "(205, 'BDS SAVINGS','BDS SAVINGS','BDS Savings'),"
                + "(220, 'SOLAR LANTERN SAVINGS','SOLAR LAN SAVINGS','Solar Lantern Savings'),"
                + "(221, 'SOLAR HOME SYSTEM SAVINGS','SOLAR HOME SAVINGS','Solar Home System Savings'),"
                + "(222, 'SIX SEATED TRY CYCLE SAVINGS','SIX STC SAVINGS','Six Seated Try cycle Savings'),"
                + "(223, 'TRI CYCLE-SOLAR SAVINGS','SOLAR TC SAVINGS','Tri Cycle-Solar Savings'),"
                + "(224, 'SOLAR WATER PUMP SAVINGS','SOLAR WP SAVINGS','Solar Water Pump Savings'),"
                + "(229, 'PRIMARY SAVINGS','PRIMARY SAVINGS','Primary Savings'),"
                + "(230, 'SPECIAL SAVINGS','SPECIAL SAVINGS','Special Savings'),"
                + "(231, 'SANITATION ENTREPRENEUR SAVINGS','SAN-ENT SAVINGS','Sanitation Entrepreneur Savings'),"
                + "(237, 'SMAP SAVINGS','SMAP SAVINGS','SMAP Savings'),"
                + "(238, 'CROSSBREED MILKING COW SAVINGS','CBMC SAVINGS','Crossbreed Milking Cow Savings'),"
                + "(241, 'MSME SAVINGS','MSME SAVINGS','MSME Savings'),"
                + "(320, 'SOLAR LANTERN CAPITAL BUILDUP SAVINGS','SOLAR LAN CBS','Solar Lantern Capital Buildup Savings'),"
                + "(321, 'SOLAR HOME SYSTEM CAPITAL BUILDUP SAVINGS','SOLAR HOME CBS','Solar Home System Capital Buildup Savings'),"
                + "(322, 'SIX SEATED TRY CYCLE CAPITAL BUILDUP SAVINGS','SIX STC CBS','Six Seated Try cycle Capital Buildup Savings'),"
                + "(323, 'TRI CYCLE-SOLAR CAPITAL BUILDUP SAVINGS','SOLAR TC CBS','Tri Cycle-Solar Capital Buildup Savings'),"
                + "(324, 'SOLAR WATER PUMP CAPITAL BUILDUP SAVINGS','SOLAR WP CBS','Solar Water Pump Capital Buildup Savings'),"
                + "(329, 'PRIMARY CAPITAL BUILDUP SAVINGS','PRIMARY CBS','Primary Capital Buildup Savings'),"
                + "(330, 'SPECIAL CAPITAL BUILDUP SAVINGS','SPECIAL CBS','Special Capital Buildup Savings'),"
                + "(331, 'SANITATION ENTREPRENEUR CAPITAL BUILDUP SAVINGS','SAN-ENT CBS','Sanitation Entrepreneur Capital Buildup Savings'),"
                + "(337, 'SMAP CAPITAL BUILDUP SAVINGS','SMAP CBS','SMAP Capital Buildup Savings'),"
                + "(338, 'CROSSBREED MILKING COW CAPITAL BUILDUP SAVINGS','CBMC CBS','Crossbreed Milking Cow Capital Buildup Savings'),"
                + "(341, 'MSME CAPITAL BUILDUP SAVINGS','MSME CBS','MSME Capital Buildup Savings'),"
                + "(999, 'BAD DEBT','BAD DEBT','Bad Debt')"
                + " ";
    }

/////////////////////////////////////////////////////////////////////////////////////////////

    String createTempRealizedGroup() {
        return "CREATE TABLE TempRealizedGroup (Id integer primary key AUTOINCREMENT NOT NULL, GroupId INT NOT NULL, GroupName STRING NOT NULL, WorkingDay INT)";
    }


}
