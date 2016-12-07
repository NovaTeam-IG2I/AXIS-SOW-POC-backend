/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.novateam.axis.sow.poc.backend.ontology;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

/**
 *
 * @author Mélody
 */
public class RegisterManager {
    private static final String NS = TDBManager.DATAMODEL_URL+"#";
    
    private TDBManager tdbm;
    
    public RegisterManager() {
        tdbm = TDBManager.getInstance();
    }
    
    public void addRegisterInstance(String name) {
        Dataset ds = tdbm.getDataset();
        ds.begin(ReadWrite.WRITE);
        Model model = ds.getDefaultModel();
        OntModel ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        OntClass class_ = ont.getOntClass(NS+"Register");
        class_.createIndividual(NS+name);
        ds.commit();
    }
    
    public void addSubRegisterInstance(String name){
        Dataset ds = tdbm.getDataset();
        ds.begin(ReadWrite.WRITE);
        Model model = ds.getDefaultModel();
        OntModel ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        OntClass class_ = ont.getOntClass(NS+"Register");
        //need to use listSubClasse for further implementation with interface
        //TODO: change exemple
        class_.getSubClass().createIndividual(NS+name);
        ds.commit();
    }
    
    public void deleteInstance(String name){
        Dataset ds = tdbm.getDataset();
        ds.begin(ReadWrite.WRITE);
        Model model = ds.getDefaultModel();
        OntModel ont = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, model);
        Resource resource = ont.getIndividual(NS+name);
        // remove statements where resource is subject
        ont.removeAll(resource, null, (RDFNode) null);
        // remove statements where resource is object
        ont.removeAll(null, null, resource);
        ds.commit();
    }
    
    public static void main(String[] args) {
        RegisterManager rm = new RegisterManager();
        rm.addRegisterInstance("Martin Luther King");
        rm.addSubRegisterInstance("Test");
        rm.deleteInstances("Test");
    }
}
