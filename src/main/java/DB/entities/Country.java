package DB.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import main.CsvRead.CsvBeanOWID;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Country {


    @Id
    @Column(name = "ISO_CODE")
    private String ISO_code;

    @Column(name = "CONTINENT")
    private String continent;

    @Column(name = "NAME")
    private String name;

    @Column(name = "POPULATION")
    private Integer population;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    private Set<CovidData> covidData = new HashSet<>();


    public Country(String ISO_code, String continent, String name, Integer population) {
        this.ISO_code = ISO_code;
        this.continent = continent;
        this.name = name;
        this.population = population;
    }

    public Country() {
    }

    private void setISO_code(String ISO_code) {
        this.ISO_code = ISO_code;
    }

    private void setContinent(String continent) {
        this.continent = continent;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setPopulation(Integer population) {
        this.population = population;
    }

    private void setISO_codeFromBean(CsvBeanOWID bean) {
        if (!bean.getISO_code().isEmpty()) {
            setISO_code(bean.getISO_code());
        } else {
            setISO_code("");
        }
    }

    private void setContinentFromBean(CsvBeanOWID bean) {
        if (!bean.getContinent().isEmpty()) setContinent(bean.getContinent());
    }

    private void setNameFromBean(CsvBeanOWID bean) {
        if (!bean.getLocation().isEmpty()) setName(bean.getLocation());
    }

    private void setPopulationFromBean(CsvBeanOWID bean) {
        if (!bean.getPopulation().isEmpty()) {
            Double populationDouble = Double.parseDouble(bean.getPopulation());
            setPopulation(populationDouble.intValue());
        }
    }

    public void set(CsvBeanOWID bean) {
        setISO_codeFromBean(bean);
        setContinentFromBean(bean);
        setNameFromBean(bean);
        setPopulationFromBean(bean);
    }

    public void addRecordCovidData(CovidData record) {
        this.covidData.add(record);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Country country = (Country) o;
        return Objects.equals(ISO_code, country.ISO_code) &&
                Objects.equals(continent, country.continent) &&
                Objects.equals(name, country.name) &&
                Objects.equals(population, country.population);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISO_code, continent, name, population);
    }

    @Override
    public String toString() {
        return ISO_code;
    }
}
