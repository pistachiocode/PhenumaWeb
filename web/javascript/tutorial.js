

var title_main_querytype_help  = "Query type selection";
var title_main_inputtype_help   = "Input type selection";
var title_main_input_help      = "Input";
var title_main_outputtype_help = "Output type selection";
var title_main_confidence_help = "Confidence selection";

var title_main_reset_help = "Reset button"
var title_main_execute_help = "Execute"



var content_main_querytype_help  = "<p>  Select between query a 'Network' or an 'Enrichment Analysis' from a input list of genes or diseases:</p><br />\n\
                                    <h5> Network: </h5>\n\
                                    <p>  Building of a network with the most significative relationships between your input list and the\n\
                                         information stored in the PhenUMA database (See 'Output Networks' in Help section for\n\
                                         further information).</p><br />\n\
                                    <h5> Enrichment Analysis: </h5>\n\
                                    <p>  Select this option to evaluate the most representative phenotypes (HPO or GO terms) of a set of genes or diseases. \n\
                                         This analysis uses an hypergeometric test taking as <b>study set</b> your input and as <b>population</b> all the information\n\
                                         annotated in the ontologies. The result is a table ordered by P-Value with the terms more enriched by your input data.</p>";


var content_main_inputtype_help   = "<p>  Selection of the type of input that you are going to provide.</p> <br />\n\
                                     <h5> Network: </h5>\n\
                                     <p>  Genes, Diseases (OMIM or Orphanet) or Phenotypes </p><br />\n\
                                     <h5> Enrichment Analysis: </h5>  \n\
                                     <p>  Genes, Diseases (OMIM or Orphanet) </p>";


var content_main_input_help      = "<p>Write the input list of elements that you want to query.<p>"+
                                   "<ul class='circle'>"+
                                    "<li> Separate your input <span class='highlighttext'>with commas</span>: GBA, OTC, KRAS. </li>"+
                                    "<li> Notice that the identifier used must be coherent with the input type selected before:"+
                                    "<ul class='circle'><li> <span class='highlighttext'>Genes:</span> we recommend <span class='highlighttext'>use EntrezGene/Geneid identifiers</span>. However, Gene symbol, Esembl, HGNC, MIM and Orphanum for genes are allowed."+
                                    "  If you choose HGNC, MIM or Orphanum identifiers you must indicate the database of reference (i.e MIM:123610, HGNC:2394 or ORPHA:120836) to avoid colision\n\
                                       between identifiers.  </li>"+
                                    "<li> <span class='highlighttext'>OMIM diseases:</span> uses the OMIM identifiers of the diseases/genes. </li>"+
                                    "<li> <span class='highlighttext'>Orphanet diseases:</span> uses the Orphanet identifiers of the diseases/genes. </li>"+
                                    "<li> <span class='highlighttext'>Phenotypes:</span> uses the HPO id of terms. PhenUMA allows the search of phenotypes by name, use the autocomplete functionality to\n\
                                          find the HPO id of your phenotype of interest.  <img src='./resources/img/tutorial/autocomplete.png' style='width:400px; height:200px;'/></li></ul></li>"+
                                   "</ul>";


var content_main_outputtype_help = "<p> Select the type of output that you want to generate:</p>"+
                                    "<ul class='circle'>"+
                                    "<li> <span class='highlighttext'> Networks </span>: The available outputs depend on the type of input selected. Although one type of relationship is selected in the form,\n\
                                     the resulting network contains all the information between genes (or diseases) included in the database (see Output Networks in Help section for further information about network building).</li>"+
                                    "<li> <span class='highlighttext'> Enrichment </span>: Select the ontology used to calculate the enrichment <span class='highlighttext'> Gene Ontology </span>(Biological Process, Molecular Function, Cellular Component) or\n\
                                    <span class='highlighttext'>Human Phenotype Ontology</span>. If you insert a list of diseases and subsequently select the Gene Ontology as output then, the enrichment is obtained using the genes related \n\
                                    to the input diseases in OMIM.</li>"+
                                    "</ul>";
var content_main_confidence_help = "<p> Select the cut-off applied over the outcome network. There are three types of confidence:</p>"+
                                    "<ul class='circle'>"+
                                    "<li> <span class='highlighttext'> Phenotypic relationships: </span> Low (top 2%), Medium (top 1%) and High (top 0.5%)</li>"+
                                    "<li> <span class='highlighttext'> Funtional relationships: </span> Low (top 0.2%), Medium (top 0.1%) and High (top 0.05%). We use more restrictive levels with functional similarities (using GO) due to the huge \n\
                                         amount of relationships included in the matrix of relationships among genes.</li>"+
                                    "</ul>";;

var content_main_reset_help = "Reset button"
var content_main_execute_help = "Execute"