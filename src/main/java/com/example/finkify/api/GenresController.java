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
    @PostConstruct
    public String getGenres() {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "SELECT ?genre WHERE {" +
                "?genre a <http://dbpedia.org/ontology/MusicGenre> ." +
                "?genre rdfs:label ?genreLabel ." +
                "FILTER(langMatches(lang(?genreLabel), 'EN'))" +
                "FILTER(?genreLabel in (\"Rock and Roll\"@en,\"Ballads\"@en,\"Hip-Hop\"@en,\"Latin music\"@en," +
                "\"Electronic Dance Music\"@en,\"Country\"@en,\"Techno\"@en,\"R&B\"@en,\"K-Pop\"@en,\"Turbo-folk\"@en," +
                "\"Metal\"@en,\"Heavy Metal\"@en,\"Rap Music\"@en,\"Hard Rock\"@en))" +
        "}";

        String endpoint = "http://dbpedia.org/sparql";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

        ResultSet results = qexec.execSelect();

        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode genre = soln.get("genre");
            findSongsByGenre(genre);
        }

        qexec.close();
        return "Done";
    }
    public String findSongsByGenre(RDFNode genre) {
        System.out.println(genre);
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX dbo: <http://dbpedia.org/ontology/>" +
                "PREFIX dbr: <http://dbpedia.org/resource/>" +
                "\n" +
                "SELECT ?music WHERE {" +
                "?music rdf:type dbo:Song ." +
                "?music dbo:genre <" + genre + "> ." +
                "}";
        String endpoint = "http://dbpedia.org/sparql";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, query);

        ResultSet results = qexec.execSelect();

        while (results.hasNext()) {
            QuerySolution soln = results.nextSolution();
            RDFNode music = soln.get("music");
            System.out.println(music);
        }

        qexec.close();
        return "Done";
    }
}
