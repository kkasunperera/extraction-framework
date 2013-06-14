package org.dbpedia.extraction.ontology

import org.dbpedia.extraction.util.Language

/**
 * Represents an object property.
 *
 * @param name The name of this property e.g. foaf:homepage
 * @param labels The labels of this entity. Map: LanguageCode -> Label
 * @param comments Comments describing this entity. Map: LanguageCode -> Comment
 * @param domain The domain of this property
 * @param range The range of this property
 * @param isFunctional Defines whether this is a functional property.
 * @param rdfTypes Defines rdf types- functional,inverseFunc, Symmetric, reflexive, irreflexive
 * A functional property is a property that can have only one (unique) value y for each instance x (see: http://www.w3.org/TR/owl-ref/#FunctionalProperty-def)
 */
class OntologyObjectProperty( name : String, labels : Map[Language, String], comments : Map[Language, String],
                              domain : OntologyClass, override val range : OntologyClass,
                              equivalentProperties : Set[OntologyProperty] = Set(),disjointWithProperties : Set[OntologyProperty]=Set(),rdfTypes : Set[OntologyProperty]=Set())
    extends OntologyProperty(name, labels, comments, domain, range, equivalentProperties,disjointWithProperties,rdfTypes )
