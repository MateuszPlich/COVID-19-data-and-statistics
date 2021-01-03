package main;

import DB.entities.Country;
import DB.entities.CovidData;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private static final Scanner INT_SCAN = new Scanner(System.in);
    private static final Scanner STRING_SCAN = new Scanner(System.in);
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("mysql_local");
    private static final EntityManager MANAGER = FACTORY.createEntityManager();
    private static String countryIso;
    private static final Date DATE_NOW = new Date();

    private static Date startDate;
    private static Date endDate;
    private static boolean errorFlag;
    private static boolean dateFormatFlag;

    public static void main(String[] args) throws ParseException {


        //===========================================temporary database========================================

//        Date date20201101 = SIMPLE_DATE_FORMAT.parse("2020-11-01");
//        Date date20201102 = SIMPLE_DATE_FORMAT.parse("2020-11-02");
//        Date date20201103 = SIMPLE_DATE_FORMAT.parse("2020-11-03");
//        Date date20201104 = SIMPLE_DATE_FORMAT.parse("2020-11-04");
//
//        Country poland = new Country("POL", "Europe", "Poland", 38000000);
//        Country usa = new Country("USA", "North America", "USA", 330000000);
//        Country australia = new Country("AUS", "Australia", "Australia", 32000000);
//
//        CovidData pol20201101 = new CovidData(poland, date20201101, 37990, 17171, 5783, 152,
//                null, 152, 4585135, 48341);
//        CovidData pol20201102 = new CovidData(poland, date20201102, 395480, 15578, 5875, 92,
//                null, 17223, 4649236, 64101);
//        CovidData pol20201103 = new CovidData(poland, date20201103, 414844, 19364, 6102, 227,
//                null, 18160, 4712224, 62988);
//        CovidData pol20201104 = new CovidData(poland, date20201104, 439536, 24692, 6475, 373,
//                null, 18654, 4779914, 67690);
//        CovidData usa20201101 = new CovidData(usa, date20201101, 9241521, 104327, 231623, 422,
//                9665, 47615, 153426532, 877936);
//        CovidData usa20201102 = new CovidData(usa, date20201102, 9324616, 83095, 232155, 532,
//                9970, 48773, 154409790, 983258);
//        CovidData usa20201103 = new CovidData(usa, date20201103, 9450988, 126372, 233720, 1565,
//                10530, 50512, 155728586, 1318796);
//        CovidData usa20201104 = new CovidData(usa, date20201104, 9554518, 103530, 234812, 1092,
//                10892, 52166, 157298430, 1569844);
//        CovidData australia20201101 = new CovidData(australia, date20201101, 27601, 6, 907, 0,
//                null, null, 8825186, null);
//        CovidData australia20201102 = new CovidData(australia, date20201102, 27610, 9, 907, 0,
//                null, null, 8855401, 30215);
//        CovidData australia20201103 = new CovidData(australia, date20201103, 27622, 12, 907, 0,
//                null, null, 8887171, 31770);
//        CovidData australia20201104 = new CovidData(australia, date20201104, 27630, 8, 907, 0,
//                null, null, 8933563, 46392);
//
//
//        MANAGER.getTransaction().begin();
//        MANAGER.persist(poland);
//        MANAGER.persist(usa);
//        MANAGER.persist(australia);
//        MANAGER.persist(pol20201101);
//        MANAGER.persist(pol20201102);
//        MANAGER.persist(pol20201103);
//        MANAGER.persist(pol20201104);
//        MANAGER.persist(usa20201101);
//        MANAGER.persist(usa20201102);
//        MANAGER.persist(usa20201103);
//        MANAGER.persist(usa20201104);
//        MANAGER.persist(australia20201101);
//        MANAGER.persist(australia20201102);
//        MANAGER.persist(australia20201103);
//        MANAGER.persist(australia20201104);
//        MANAGER.getTransaction().commit();


        //===========================================temporary database========================================

        initialMethod();

    }


    private static void initialMethod() {

        System.out.println("COVID-19: DATA AND STATISTIC");
        System.out.println("Select Country (name by ISO CODE eg. POL for Poland). Press H for list of ISO_CODES" +
                " or press Q to end program");
        countryIso = STRING_SCAN.next();
        if (countryIso.equalsIgnoreCase("q")) System.exit(0);
        if (countryIso.equalsIgnoreCase("h")) {
            displayAvailableCountries();
            STRING_SCAN.reset();
            initialMethod();
        }
        else {
            selectStartDate(INT_SCAN);
            selectEndDate(INT_SCAN);
            availableOptions(INT_SCAN);
        }


        FACTORY.close();

    }


    private static void availableOptions(Scanner scan) {
        System.out.println("1.Total cases\n2.Daily new cases\n3.Total deaths\n4.Daily new deaths\n5.ICU Patients" +
                "\n6.Hospitalized Patients+\n7.Total_tests\n8.Daily new tests\n9.Get all data");
        errorFlag = true;
        do {
            try {
                errorFlag = false;
                int i = scan.nextInt();
//                int i = Integer.parseInt(scan.nextLine());
                switch (i) {
                    case 1:
                        selectTotalCases();
                        break;
                    case 2:
                        selectDailyNewCases();
                        break;
                    case 3:
                        selectTotalDeaths();
                        break;
                    case 4:
                        selectDailyNewDeaths();
                        break;
                    case 5:
                        selectIcuPatients();
                        break;
                    case 6:
                        selectHospitalizedPatients();
                        break;
                    case 7:
                        selectTotalTests();
                        break;
                    case 8:
                        selectDailyNewTests();
                        break;
                    case 9:
                        selectAllData();
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong input format. Try again.");
                scan.reset();
            }
        } while (errorFlag);
    }

    private static void selectTotalCases() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM OWID o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.print("| Date: " + covidData.getDate());
            System.out.print(" | iso_code: " + covidData.getCountry());
            System.out.println(" | total_cases: " + covidData.getTotal_cases() + " |");
        }
        MANAGER.close();
    }

    private static void selectDailyNewCases() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM CovidData o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.print("| Date: " + covidData.getDate());
            System.out.print(" | iso_code: " + covidData.getCountry());
            System.out.println(" | daily_new_cases: " + covidData.getNew_cases() + " |");
        }
        MANAGER.close();
    }

    private static void selectTotalDeaths() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM CovidData o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.print("| Date: " + covidData.getDate());
            System.out.print(" | iso_code: " + covidData.getCountry());
            System.out.println(" | total_deaths: " + covidData.getTotal_deaths() + " |");
        }
        MANAGER.close();
    }

    private static void selectDailyNewDeaths() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM CovidData o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.print("| Date: " + covidData.getDate());
            System.out.print(" | iso_code: " + covidData.getCountry());
            System.out.println(" | daily_new_deaths: " + covidData.getNew_deaths() + " |");
        }
        MANAGER.close();
    }

    private static void selectIcuPatients() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM OWID o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.print("| Date: " + covidData.getDate());
            System.out.print(" | iso_code: " + covidData.getCountry());
            System.out.println(" | ICU Patients: " + covidData.getIcu_patients() + " |");
        }
        MANAGER.close();
    }

    private static void selectHospitalizedPatients() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM CovidData o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.print("| Date: " + covidData.getDate());
            System.out.print(" | iso_code: " + covidData.getCountry());
            System.out.println(" | Hospitalized Patients: " + covidData.getHosp_patients() + " |");
        }
        MANAGER.close();
    }

    private static void selectTotalTests() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM CovidData o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.print("| Date: " + covidData.getDate());
            System.out.print(" | iso_code: " + covidData.getCountry());
            System.out.println(" | Total tests: " + covidData.getTotal_tests() + " |");
        }
        MANAGER.close();
    }

    private static void selectDailyNewTests() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM CovidData o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.print("| Date: " + covidData.getDate());
            System.out.print(" | iso_code: " + covidData.getCountry());
            System.out.println(" | Daily new tests: " + covidData.getNew_tests() + " |");
        }
        MANAGER.close();
    }

    private static void selectAllData() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT * FROM CovidData o WHERE o.ISO_CODE=? AND o.date BETWEEN ? AND ?", CovidData.class);
        q.setParameter(1, countryIso);
        q.setParameter(2, startDate);
        q.setParameter(3, endDate);

        List<CovidData> cdList = q.getResultList();
        for (CovidData covidData : cdList) {
            System.out.printf("| Date: %s | Country: %s | Total Cases: %d | Daily New Cases: %d |\n" +
                            "Total deaths: %d | Daily new deaths: %d | ICU patients: %d |\n" +
                            "Hospitalized patients: %d | Total Tests: %d | Daily new tests: %d |\n\n", covidData.getDate(), covidData.getCountry(),
                    covidData.getTotal_cases(), covidData.getNew_cases(), covidData.getTotal_deaths(), covidData.getNew_deaths(), covidData.getIcu_patients(),
                    covidData.getHosp_patients(), covidData.getTotal_tests(), covidData.getNew_tests());
        }
        MANAGER.close();
    }

    private static void displayAvailableCountries() {
        MANAGER.getTransaction().begin();
        Query q = MANAGER.createNativeQuery("SELECT ISO_CODE FROM Country", Country.class);


        List<Country> countries = q.getResultList();
        for (Country country : countries) {
            System.out.println(" | iso_code: " + country.getISO_code());
        }
        MANAGER.close();
    }

//    private static Date selectFirstAvailableDate(String countryIso){
//        manager.getTransaction().begin();
//        Query q = manager.createNativeQuery("SELECT MIN(o.DATE) FROM OWID o WHERE o.ISO_CODE=?", OWID.class);
//        q.setParameter(1, countryIso);
//        return (Date)q.getSingleResult();
//    }

    private static Date readDate(Scanner scan) throws ParseException {
        SIMPLE_DATE_FORMAT.setLenient(false);
        return SIMPLE_DATE_FORMAT.parse(scan.next());
    }


    private static Date selectStartDate(Scanner scan) {
        dateFormatFlag = true;
        do {
            try {
                dateFormatFlag = false;
                System.out.println("Select start date: (yyyy-MM-dd)");
                startDate = readDate(scan);
            } catch (ParseException e) {
                System.out.println("Wrong date format (yyyy-MM-dd). Try again.");
                dateFormatFlag = true;
                scan.reset();
            }
        }
        while (dateFormatFlag);
        return startDate;
    }

    private static void selectEndDate(Scanner scan) {
        dateFormatFlag = true;
        do {
            try {
                dateFormatFlag = false;
                System.out.println("Select end date: (yyyy-MM-dd)");
                endDate = readDate(scan);
            } catch (ParseException e) {
                System.out.println("Wrong date format (yyyy-MM-dd). Try again.");
                dateFormatFlag = true;
                scan.reset();
            }
        }
        while (dateFormatFlag);

    }
}
