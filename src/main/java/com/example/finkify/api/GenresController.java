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
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT ?genreLabel WHERE {" +
                "?genre a <http://dbpedia.org/ontology/MusicGenre> ." +
                "?genre rdfs:label ?genreLabel ." +
                "FILTER(langMatches(lang(?genreLabel), 'EN'))" +
                "FILTER(?genreLabel in (\"Rock and Roll\"@en,\"Ballads\"@en,\"Hip-Hop\"@en,\"Latin music\"@en," +
                "\"Electronic Dance Music\"@en,\"Country\"@en,\"Techno\"@en,\"R&B\"@en,\"K-Pop\"@en,\"Turbo-folk\"@en," +
                "\"Metal\"@en,\"Heavy Metal\"@en,\"Rap Music\"@en))" +
        "}";



        String endpoint = "http://dbpedia.org/sparql";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

        ResultSet results = qexec.execSelect();

        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode genreLabel = soln.get("genreLabel");
            System.out.println(genreLabel);
        }

        qexec.close();
        return "Done";
    }
}
