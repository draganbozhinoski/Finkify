package com.example.finkify.api;

import jakarta.annotation.PostConstruct;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GenresController {
    @GetMapping
    public String getGenres() {
        // Define the SPARQL query
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT ?genreLabel WHERE {" +
                "?genre a <http://dbpedia.org/ontology/MusicGenre> ." +
                "?genre rdfs:label ?genreLabel ." +
                "FILTER(langMatches(lang(?genreLabel), 'EN'))" +
                "}";

        // Set up the DBpedia SPARQL endpoint
        String endpoint = "http://dbpedia.org/sparql";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

        // Execute the query
        ResultSet results = qexec.execSelect();

        // Print out the results
        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode genreLabel = soln.get("genreLabel");
            System.out.println(genreLabel);
        }

        // Clean up resources
        qexec.close();
        return "Done";
    }
}
