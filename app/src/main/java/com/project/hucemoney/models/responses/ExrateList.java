package com.project.hucemoney.models.responses;

import com.project.hucemoney.entities.Exrate;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ExrateList", strict = false)
public class ExrateList {

    @Element(name = "DateTime")
    private String dateTime;

    @ElementList(inline = true, entry = "Exrate")
    private List<Exrate> exrateList;

    public String getDateTime() {
        return dateTime;
    }

    public List<Exrate> getExrateList() {
        return exrateList;
    }
}
