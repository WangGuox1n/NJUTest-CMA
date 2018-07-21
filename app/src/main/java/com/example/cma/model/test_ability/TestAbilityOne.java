package com.example.cma.model.test_ability;

/**
 * Created by new on 2018/7/16.
 */

public class TestAbilityOne {
    private Long year;
    private Long id;

    public TestAbilityOne(Long year, Long id, String production, String ability, String reference)
    {
        this.year=year;
        this.id=id;
        this.productionName=production;
        this.ability=ability;
        this.referrence=reference;
    }
    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }



    private String productionName;
    private String ability;

    public String getProductionName() {
        return productionName;
    }

    public void setProductionName(String productionName) {
        this.productionName = productionName;
    }

    public String getReferrence() {
        return referrence;
    }

    public void setReferrence(String referrence) {
        this.referrence = referrence;
    }

    private String referrence;

}
