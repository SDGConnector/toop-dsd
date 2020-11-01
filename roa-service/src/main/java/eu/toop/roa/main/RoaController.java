package eu.toop.roa.main;

import eu.toop.roa.model.Address;
import eu.toop.roa.model.Agent;
import io.jooby.Context;
import io.jooby.annotations.GET;
import io.jooby.annotations.Path;

import java.util.Arrays;
import java.util.List;

@Path("/rest")
public class RoaController {
  @GET("/roaList")
  public List<Agent> getAgents(Context context) {
    context.setResponseType("text/json");
    return Arrays.asList(
        new Agent("RE238912371", "VAT", "aCompanyName1", new Address("fullAddress1", "street1", "building1", "town1", "PC1", "TR1")),
        new Agent("RE238912372", "VAT", "aCompanyName2", new Address("fullAddress2", "street2", "building2", "town2", "PC2", "TR2")),
        new Agent("RE238912373", "VAT", "aCompanyName3", new Address("fullAddress3", "street3", "building3", "town3", "PC3", "TR3")),
        new Agent("RE238912374", "VAT", "aCompanyName4", new Address("fullAddress4", "street4", "building4", "town4", "PC4", "TR4")),
        new Agent("RE238912375", "VAT", "aCompanyName5", new Address("fullAddress5", "street5", "building5", "town5", "PC5", "TR5"))
    );
  }
}
