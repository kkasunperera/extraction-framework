package org.dbpedia.extraction.ontology

import org.dbpedia.extraction.util.Language
import org.dbpedia.extraction.wikiparser.{TextNode, PropertyNode, TemplateNode}

/**
 * Represents an ontology property.
 * There are 2 sub classes of this class: OntologyObjectProperty and OntologyDatatypeProperty.
 *
 * @param name The name of this entity. e.g. foaf:name
 * @param labels The labels of this entity. Map: LanguageCode -> Label
 * @param comments Comments describing this entity. Map: LanguageCode -> Comment
 * @param range The range of this property
 * @param isFunctional Defines whether this is a functional property.
 * @param rdfTypes Defines rdf types- functional,inverseFunc, Symmetric, reflexive, irreflexiv
 * A functional property is a property that can have only one (unique) value y for each instance x (see: http://www.w3.org/TR/owl-ref/#FunctionalProperty-def)
 */
class OntologyProperty(
  name: String,
  labels: Map[Language, String],
  comments: Map[Language, String],
  val domain: OntologyClass,
  val range: OntologyType,
//  val isFunctional: Boolean,
  val equivalentProperties: Set[OntologyProperty],
  val disjointWithProperties : Set[OntologyProperty],
  val rdfTypes: Set[OntologyProperty]
)
extends OntologyEntity(name, labels, comments)
{
    require(! RdfNamespace.validate(name) || domain != null, "missing domain for property "+name)
    require(! RdfNamespace.validate(name) || range != null, "missing range for property "+name)
    require(equivalentProperties != null, "missing equivalent properties for property "+name)
    require(disjointWithProperties !=null,"missing disjoint properties for property"+ name)
    require(rdfTypes != null, "missing rdfType properties(functional,InverseFunctional, symmetric, reflexive, Irreflexive) for property" + name)
    
    val uri = RdfNamespace.fullUri(DBpediaNamespace.ONTOLOGY, name)

    val isExternalProperty = ! uri.startsWith(DBpediaNamespace.ONTOLOGY.namespace)
    
    override def toString = uri

    override def equals(other : Any) = other match
    {
        case otherProperty : OntologyProperty => (name == otherProperty.name)
        case _ => false
    }

    override def hashCode = name.hashCode

    def isFunctional(node : TemplateNode) : Boolean = readTemplateProperty(node, "rdf:type") match
    {
      case Some(text) if text == "owl:FunctionalProperty" => true
      case Some(text) => false
      case None => false
    }

  private def readTemplateProperty(node : TemplateNode, propertyName : String) : Option[String] =
  {
    node.property(propertyName) match
    {
      case Some(PropertyNode(_, TextNode(text, _) :: Nil, _)) if !text.trim.isEmpty => Some(text.trim)
      case _ => None
    }
  }

}
