
//TODO: Definir como globales
var gene = 1;
var disease = 2;
var orphanum = 3;

//relationshiptype
var simsemhpo = 0;
var simsemgobp = 1;
var simsemgocc = 2;
var simsemgomf = 3;
var infomim = 4;
var inforpha = 5;
var infgene = 6;
var ppi = 7;
var metabolic = 8;
var omim = 9;
var orphadata = 10;
var queryer = 11;
var querydisease = 12;
var querygene =  13;


//Input Type Constants

var INPUTGENES = 0;
var INPUTOMIM  = 1;
var INPUTORPHA = 2;
var INPUTPHENOTYPES = 3;

var NETWORKQUERY = 0;
var ENRICHMENTQUERY = 1;


function getLink(data)
{
   return getLink(data, data.type)
}

function getLink(data, type)
{

    
    if(type == disease)
    {
        return "http://omim.org/entry/"+data.id
    }
    else if(type == gene)
    {
       return "http://www.ncbi.nlm.nih.gov/gene/"+data.id     
    }
    else if(type >= orphanum)
    {
       return "http://www.orpha.net/consor/cgi-bin/OC_Exp.php?lng=EN&Expert="+data.id     
    }
    
    return "";
}


function getIcon(data)
{
    if (data.type == gene_type)
    {
        return "phenumageneicon"
    }
    else if (data.type == disease_type)
    {
        return "phenumadiseaseicon"
    }
    else if (data.type >= orphanum_type)
    {
        return "phenumaorphaneticon"
    }
           
    return "";
}


function getDatabase(data)
{
    if (data.type == gene_type)
    {
        return "Entrez"
    }
    else if (data.type == disease_type)
    {
        return "OMIM"
    }
    else if (data.type >= orphanum_type)
    {
        return "Orphanum"
    }
           
    return "";
}


function getRelationshipType(type)
{

    if (type == simsemgobp)
    {
        return "Functional Similarity (Biological Process)"
    }
    else if (type == simsemgocc)
    {
        return "Functional Similarity (Cellular Component)"
    }
    else if (type == simsemgomf)
    {
        return "Functional Similarity (Molecular Function)"
    }
    else if (type == simsemhpo)
    {
        return "Phenotypic Similarity"
    }
    else if (type == ppi)
    {
        return "Protein-Protein Interaction"
    }
    else if (type == metabolic)
    {
        return "Metabolic Interaction"
    }
    else if (type == infgene)
    {
        return "Inferred Relationship by Genes"
    }
    else if (type == infomim)
    {
        return "Inferred Relationship by OMIM"
    }
    else if (type == inforpha)
    {
        return "Inferred Relationship by Orphan Diseases"
    }
    else if (type == omim)
    {
        return "Known Relationship from OMIM"
    }
    else if (type == orphadata)
    {
        return "Known Relationship from Orphanet"
    }
   
    
    return "";
}

function getRelationshipTypeStyle(type)
{

    if (type == simsemgobp)
    {
        return "functionalbprel"
    }
    else if (type == simsemgocc)
    {
        return "functionalccrel"
    }
    else if (type == simsemgomf)
    {
        return "functionalmfrel"
    }
    else if (type == simsemhpo)
    {
        return "phenotypicrel"
    }
    else if (type == ppi)
    {
        return "ppirel"
    }
    else if (type == metabolic)
    {
        return "metabolicrel"
    }
    else if (type == infgene)
    {
        return "inferredbygenerel"
    }
    else if (type == infomim)
    {
        return "inferredbyomimrel"
    }
    else if (type == inforpha)
    {
        return "inferredbyorpharel"
    }
    else if (type == omim)
    {
        return "omimrel"
    }
    else if (type == orphadata)
    {
        return "orphadatarel"
    }
   
    
    return "";
}


function getSource(data)
{
      if (data.type == simsemgobp)
    {
        return "Gene Ontology (Biological Process)"
    }
    else if (data.type == simsemgocc)
    {
        return "Gene Ontology (Cellular Component)"
    }
    else if (data.type == simsemgomf)
    {
        return "Gene Ontology (Molecular Function)"
    }
    else if (data.type == simsemhpo)
    {
        return "Human Phenotype Ontology"
    }
    else if (data.type == ppi)
    {
        return "STRING"
    }
    else if (data.type == metabolic)
    {
        return "Veeramani et al."
    }
    else if (data.type == infgene)
    {
        return "OMIM/Orphanet"
    }
    else if (data.type == infomim)
    {
        return "OMIM (Gene Map)"
    }
    else if (data.type == inforpha)
    {
        return "Orphanet"
    }
    else if (data.type == omim)
    {
        return "OMIM (Gene Map)"
    }
    else if (data.type == orphadata)
    {
        return "Orphanet"
    }
    
    return "";
}


function getPrevalence(type)
{
    if(type==orphanum_type)
    {
        return ">1/1000";
    }
    else if(type==orphanum_low_1_type)
    {
       return "1-5/10000";    
    }
    else if(type==orphanum_low_2_type)
    {
       return "1-9/100000";       
    }
    else if(type==orphanum_low_3_type)
    {
       return "1-9/1000000";        
    }    
    else if(type==orphanum_low_4_type)
    {
       return "<1/1000000";    
    }
}

function toggleview(form, form2)
{
    
    if($(form).is(':visible'))
    {
        $(form).hide(); 
        $(form2).show();

        $("button[id='queryform:changeview'] > span").html("View Network");
        datatable("");
    }
    else
    {
        $("button[id='queryform:changeview'] > span").html("View Table");
        $(form2).hide();
        $(form).show();
        
    }

}


function showView(form)
{
    $(form).show();
}

function hideView(form)
{
    $(form).hide();
}

function infoblack(text)
{
    return "<img src='./resources/img/info_black.png' height='20' width='20' alt='"+text+"'/>";
}


function insertLinkHTML(data, text)
{
    return insertLinkHTML(data, text, data.type)
}

function insertLinkHTML(data, text, type)
{
    return "<a class='qtiplinks' href=\""+getLink(data, type)+"\" target='_blank'>"+text+" </a>";
}


function enrichmenttable(filter)
{
    var enrichment = eval("(" + $("input[name='falseform:enrichment']").val() + ")");

    var table_header = "<thead class='phenumaresult_header'> <th style='width:15px;'></th>" +
                            "<th><a class='phenumaresult_title' onclick=\"SortDetailedTable(1, 'resulttable', 'nodo1', 'text'); href='javascript:;'\">Term ID</a></th>" +
                            "<th><a class='phenumaresult_title' onclick=\"SortDetailedTable(2, 'resulttable', 'nodo2', 'text'); href='javascript:;'\">Name</a></th>" +
                            "<th><a class='phenumaresult_title' onclick=\"SortDetailedTable(3, 'resulttable', 'aso', 'pairs'); href='javascript:;'\">Annotated Study</a></th>" +
                            "<th><a class='phenumaresult_title' onclick=\"SortDetailedTable(4, 'resulttable', 'apo', 'pairs'); href='javascript:;'\">Annotated Population</a></th>" +
                            "<th><a class='phenumaresult_title' onclick=\"SortDetailedTable(5, 'resulttable', 'pvalue', 'scientific'); href='javascript:;'\">P-Value</a></th>" +
                        "</thead>";

     var table_body = "<tbody>";

     var data;

     //var keys = Object.keys(network);

    
    for( var row in enrichment.enrichment)
    {
        data = enrichment.enrichment[row]
         
        var termid   = passFilter(data.termid, filter);
        var termname = passFilter(data.termname, filter);
        
        
        if (termid != null || termname != null)
        {
            if(termid==null)
                termid = data.termid

            if(termname==null)
                termname = data.termname
            
            table_body = table_body + "<tr class='phenumaresult_row'><td style='width:15px;' class='ui-icon ui-icon-circle-plus'></td>"+
                                      "<td class='phenumaresult_data'>"+termid+"</td>"+
                                      "<td class='phenumaresult_data'>"+termname+"</td>"+
                                      "<td class='phenumaresult_data'>"+data.aso+"</td>"+
                                      "<td class='phenumaresult_data'>"+data.apo+"</td>"+
                                      "<td class='phenumaresult_data'>"+data.pvalue+"</td></tr>"
            
            table_body = table_body + "<tr class='detail'><td colspan='6' style='width:100%'><table class='detail'>";
            
            for(var obj in data.objects)
            {
                 table_body = table_body +"<tr><td><a href='"+getLink(data.objects[obj], data.objects[obj].type)+"' target='_blank'>"+data.objects[obj].id+"</a></td><td> "+data.objects[obj].name+"</td></tr>";   
            }
            
            table_body = table_body + "</table></td></tr>";
        }
     }

     table_body = table_body+"</tbody>";


     $("#resulttable").html(table_header+table_body);

}

function datatable(filter)
{
    if($('#layout_table').is(':visible'))
    {
        var network = getSubnetworks();

        var table_header = "<thead class='phenumaresult_header'> " +
                                "<th><a class='phenumaresult_title' onclick=\"SortTable(0, 'resulttable', 'nodo1', 'link'); href='javascript:;'\">Nodo1</a></th>" +
                                "<th><a class='phenumaresult_title' onclick=\"SortTable(1, 'resulttable', 'nodo2', 'link'); href='javascript:;'\">Nodo2</a></th>" +
                                "<th><a class='phenumaresult_title' onclick=\"SortTable(2, 'resulttable', 'input', 'text'); href='javascript:;'\">Input</a></th>" +
                                "<th><a class='phenumaresult_title' onclick=\"SortTable(3, 'resulttable', 'reltype', 'p'); href='javascript:;'\">Type of Relationship</a></th>" +
                                "<th><a class='phenumaresult_title' onclick=\"SortTable(4, 'resulttable', 'score', 'float'); href='javascript:;'\">Score</a></th>" +
                                "<th><a class='phenumaresult_title' onclick=\"SortTable(5, 'resulttable', 'source', 'text'); href='javascript:;'\">Source</a></th>" +
                            "</thead>";

         var table_body = "<tbody>";

         var data;

         //var keys = Object.keys(network);

         for( var subnetwork in network)
         {
             var subnetwork_index = 0;

             while ( subnetwork_index < network[subnetwork].length)
             {
                 /**
                  * Get subnetwork data.
                  */
                 data = network[subnetwork][subnetwork_index].data

                 /**
                  * Find nodes in the current network.
                  */
                 var source = findObject(data.source)
                 var target = findObject(data.target)

                 /**
                  * Indicate which node is part of 
                  * the input query. 
                  */
                 var input = "-"; 
                 if(source.input && target.input)   input = "Both";
                 else if (source.input)             input = "Node1";
                 else if (target.input)             input = "Node2";


                 /**
                  * User is allowed to search data in the table
                  * and applied filters. This code applies style 
                  * to the data in case of any filter is applied.
                  */
                 var node1 = passFilter(source.label, filter);
                 var node2 = passFilter(target.label, filter);
                 var relationship = passFilter(getRelationshipType(data.type), filter);

                 
                 /**
                  * Creates table row for the HTML output table.
                  * && (node1!=null || node2!=null || relationship!=null)
                  */
                 if (source !== null  && target !== null )
                 {
                    if(node1 === null)
                        node1 = source.label;

                    if(node2=== null)
                        node2 = target.label;

                    if(relationship === null)
                        relationship = getRelationshipType(data.type);


                    if(node1 !== null && node1 !== "" && 
                       node2 !== null && node2 !== "" )  
                    {

                        table_body = table_body + "<tr class='phenumaresult_row'>"+
                                                    "<td class='phenumaresult_data'><a class='phenumaresult_data' href='"+getLink(source, source.type)+"' target='_blank'>"+node1+"</a></td>"+
                                                    "<td class='phenumaresult_data'><a class='phenumaresult_data' href='"+getLink(target, target.type)+"' target='_blank'>"+node2+"</a></td>"+
                                                    "<td class='phenumaresult_data'>"+input+"</td>"+
                                                    "<td class='phenumaresult_data'><p class='"+getRelationshipTypeStyle(data.type)+"'>"+relationship+"</p></td>"+
                                                    "<td class='phenumaresult_data'>"+data.score+"</td>"+
                                                    "<td class='phenumaresult_data'>"+getSource(data)+"</td>"+
                                                   "</tr>";
                    }
                 }

                 subnetwork_index++;
            }
         }

         table_body = table_body+"</tbody>";

         $("#resulttable").html(table_header+table_body);
    }
}

function updatetable(querytype)
{
    console.log("updatetable: querytype="+querytype)
    
    if(querytype == 0)
    {
        toggleview("#layout", "#layout_table")
        datatable('')
    }
    else
    {
        showView("#layout_table")
        hideView("#layout")
        enrichmenttable('')
        tableExpansion()
    }
}

/**
 * This function apply to the data a stablished filter by the user. 
 * 
 * This code highligths (in red) the substring of the a row value (data)
 * that matchs with a defined filter. To do that, the string
 * is divided in tree parts: begining, found, final.
 * 
 * For example: for datavalue = "Friedrich disease" and a filter = "dis" 
 * this code returns:
 * 
 * Fiedrich <span style='color:red;'> dis </span> ease
 * 
 * The objective is indicate which part of the data match with a
 * filter applied by the user.
 * 
 * @param {type} data
 * @param {type} filter
 * @returns {String}
 */
function passFilter(data, filter)
{
    /**
     *  For an empty filter the data style keeps
     *  unchanged.
     */
    if (filter == "")
        return data;
    
    
    
    var lowerfilter = filter.toLowerCase();
    var lowerdata   =  data.toLowerCase();
    
    var found       = lowerdata.indexOf(lowerfilter);
    
    if(found!=-1)
    {
        /**
         * This code highligths (in red) the substring of the row value
         * that match with a defined filter. To do that, the string
         * is divided in tree parts: begining, found, final.
         * 
         * For example: for datavalue = "Friedrich disease" and a filter = "dis" 
         * this code returns:
         * 
         * Fiedrich <span style='color:red;'> dis </span> ease
         * 
         * The objective is indicate which part of the data match with the
         * selected filter.
         * 
         */
        var begining = data.substring(0, found);
        var founded = data.substring(found, found+filter.length);
        var final = data.substring(found+filter.length, data.length);

        return begining + "<span style='color:red;'>"+founded+"</span>"+final;
    }
    
    return  null;
}

function filterTable(data, querytype)
{
    console.log("updatetable: filterTable="+querytype)
    if(querytype == 0)
        datatable(data)
    else
        enrichmenttable(data)
}


function getNodeInformation(node){
    $("input[name = 'falseform:selectedNode']").val(node)
    $("a[id = 'falseform:buttonqtips']").click();
    
}

function getEdgeInformation(edge){

    $("input[name = 'falseform:selectedEdge']").val(edge)
    $("a[id = 'falseform:buttonedgeqtips']").click();
    
}
function getQtipInformaionJSON(){
    
     var data =  $("input[name = 'falseform:qtipinformation']").val();
     return data;
}

function cleanPhenotypesJSON(){
    $("input[name = 'falseform:qtipinformation']").val('');
}



/**
 * Find an object in the current network.
 * 
 * @param {type} id
 * @returns {network.data.nodes|findObject.nodes}
 */
function findObject(id)
{   
    var network = getNetwork();
    
    var nodes = network.data.nodes
    
    var i = network.data.nodes.length - 1;
    
    while(i>=0  && nodes[i].id != id)
        i--;
       
    if(i>=0)
        return nodes[i]
    
    return null;
}


/**
 * Checks if an array is empty.
 * 
 * @param {type} object_array
 * @returns {Boolean}
 */
function isEmpty(object_array)
{
    var count = 0;
    for(var i in object_array)
        count++
    
    return (count == 0);
}


//Pagination

function pagination(show_per_page, table)
{
	//getting the amount of elements inside content div
	var number_of_items = $(table+' > tbody').children().size();
        
     //   alert(number_of_items)
        
	//calculate the number of pages we are going to have
	var number_of_pages = Math.ceil(number_of_items/show_per_page);
	
	//set the value of our hidden input fields
	$('#current_page').val(0);
	$('#show_per_page').val(show_per_page);
	
	//now when we got all we need for the navigation let's make it '
	
	/* 
	what are we going to have in the navigation?
		- link to previous page
		- links to specific pages
		- link to next page
	*/
	var navigation_html = '<a class="previous_link" href="javascript:previous(\''+table+'\');">Prev</a>';
	var current_link = 0;
	while(number_of_pages > current_link){
		navigation_html += '<a class="page_link" href="javascript:go_to_page(' + current_link +',\''+table+'\')" longdesc="' + current_link +'">'+ (current_link + 1) +'</a>';
		current_link++;
	}
	navigation_html += '<a class="next_link" href="javascript:next(\''+table+'\');">Next</a>';
	
	$('#page_navigation').html(navigation_html);
	
	//add active_page class to the first page link
	$('#page_navigation .page_link:first').addClass('active_page');
	
	//hide all the elements inside content div
	$(table+' > tbody').children().css('display', 'none');
	
	//and show the first n (show_per_page) elements
	$(table+' > tbody').children().slice(0, show_per_page).css( 'display', 'inline' );
}


function previous(table){
	
	new_page = parseInt($('#current_page').val()) - 1;
	//if there is an item before the current active link run the function
	if($('.active_page').prev('.page_link').length==true){
		go_to_page(new_page, table);
	}
	
}

function next(table){
	new_page = parseInt($('#current_page').val()) + 1;
	//if there is an item after the current active link run the function
	if($('.active_page').next('.page_link').length==true){
		go_to_page(new_page, table);
	}
	
}

function go_to_page(page_num, table){
	//get the number of items shown per page
	var show_per_page = parseInt($('#show_per_page').val());
	
        
	//get the element number where to start the slice from
	start_from = page_num * show_per_page;
	
	//get the element number where to end the slice
	end_on = start_from + show_per_page;
	
	//hide all children elements of content div, get specific items and show them
	$(table+' > tbody').children().css('display', 'none').slice(start_from, end_on).css('display', 'inline');
	
	/*get the page link that has longdesc attribute of the current page and add active_page class to it
	and remove that class from previously active page link*/
	$('.page_link[longdesc=' + page_num +']').addClass('active_page').siblings('.active_page').removeClass('active_page');
	
	//update the current page input field
	$('#current_page').val(page_num);
}


 /**
  * Main Examples 
  */
   function ejemplo1()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("OTC")              
        $("input[id='queryform:inputtype:0']").click()         
        $("button[id='queryform:buildnetworkbtn']").click()


        pbAjax.cancel();
        pbAjax.start();
    }

    function ejemplo2()
    {     
        $("input[id='queryform:inputtype:1']").click()      
        $("textarea[id = 'queryform:inputtxtarea']").val("271980")              


        $("button[id='queryform:buildnetworkbtn']").click()


        pbAjax.cancel();
        pbAjax.start();
    }

    function ejemplo3()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("95")              
        $("input[id='queryform:inputtype:2']").click()         


        $("button[id='queryform:buildnetworkbtn']").click()

        pbAjax.cancel();
        pbAjax.start();
    }

    function ejemplo4()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("HP:0001691, HP:0003116, HP:0001953")              
        $("input[id='queryform:inputtype:3']").click()         
        $("button[id='queryform:buildnetworkbtn']").click()

        pbAjax.cancel();
        pbAjax.start();
    }
    
    
    function ejemplo_genes_network()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("OTC")              
        $("input[id='queryform:inputtype:0']").click()         
        $("button[id='queryform:buildnetworkbtn']").click()

    }

    function ejemplo_omim_network()
    {     
        $("input[id='queryform:inputtype:1']").click()      
        $("textarea[id = 'queryform:inputtxtarea']").val("271980")              


        $("button[id='queryform:buildnetworkbtn']").click()

    }

    function ejemplo_orpha_network()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("95")              
        $("input[id='queryform:inputtype:2']").click()         


        $("button[id='queryform:buildnetworkbtn']").click()

    }

    function ejemplo_phenotypes_network()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("HP:0001691, HP:0003116, HP:0001953")              
        $("input[id='queryform:inputtype:3']").click()         
        $("button[id='queryform:buildnetworkbtn']").click()

    }


    function ejemplo_genes_enrichment()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("BRAF, MAP2K1, MAP2K2, KRAS, SLC17A5")              
        $("input[id='queryform:inputtype:0']").click()         
        $("button[id='queryform:buildnetworkbtn']").click()
    }

    function ejemplo_omim_enrichment()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("607681, 607628, 611364, 600669, 608096, 607631, 607208, 300423, 600131, 604827, 608217")              
        $("input[id='queryform:inputtype:1']").click()         
        $("button[id='queryform:buildnetworkbtn']").click()
    }            

    function ejemplo_orpha_enrichment()
    {          
        $("textarea[id = 'queryform:inputtxtarea']").val("67042, 179, 52427, 886, 231183, 773, 85128, 99001, 99002, 1873, 1871, 791, 227796, 53540, 75376")              
        $("input[id='queryform:inputtype:2']").click()         
        $("button[id='queryform:buildnetworkbtn']").click()
    }




    function changeTextAreaValue(val)
    {          
        if(val == 0)
            $("span[id = 'queryform:inputtxtarea_label']").html("Write a list of genes i.e.  GBA, OTC  or  2629, 5009:")       
        else if (val == 1)
            $("span[id = 'queryform:inputtxtarea_label']").html("Write a list of OMIM genes/diseases. Please use OMIM identifiers i.e  271980, 607631:")       
        else if (val == 2)
            $("span[id = 'queryform:inputtxtarea_label']").html("Write a list of orphan diseases. Please use Orphanet identifiers i.e. 95, 3092, 99051:")       
        else if (val == 3){
            $("span[id = 'queryform:inputtxtarea_label']").html("Write a list of phenotypes. Please use HPO id i.e. HP:0001691, HP:0003116, HP:0001953:")       
        }

        bindAutocomplete("main", val);
    }


    function toggleFormType(val)
    {
        if(val == 0)
        {
            $("table[id = 'queryform:output_networktypepanel']").show();
            $("table[id = 'queryform:output_enrichmenttypepanel']").hide();

            $("table[id = 'table_networkexamples']").show();
            $("table[id = 'table_enrichmentexamples']").hide();
        }
        else if (val == 1)
        {
            $("table[id = 'queryform:output_enrichmenttypepanel']").show();
            $("table[id = 'queryform:output_networktypepanel']").hide();

            $("table[id = 'table_networkexamples']").hide();
            $("table[id = 'table_enrichmentexamples']").show();
        }
    }
            
            
          
    function closeModal()
    {
        $.modal.close();
    }
    
    function showModalInfo(text)
    {
        $.modal("<div><center><img src='./resources/img/info_black.png' style='width:60px; height: 60px'/> <p style='font-size:18px;'> "+text+" <p> </center></div>")
    }

    function showModalProcessing(title, subtitle)
    {
        $.modal("<div><center><p class='title_modal'> "+title+" </p> <p class='subtitle_modal'> "+subtitle+" </p> <img src='./resources/img/loader.GIF' style='width:40px; height: 40px; padding-top:10px;'/></center></div>")
    }
    

    function showModalWait()
    {
        showModalProcessing("Please wait.", "")
    }


    function showModalDownload()
    {
        closeModal()
        $('#basic-modal-download').modal();
    }

    function showModalDownloadEnrichment()
    {
        closeModal()
        $('#basic-modal-download-enrichment').modal();
    }
    
    
    function showModalHelp(title, content, width, height)
    {
        var title_html   = "<h4 class='modal'>"+title+"</h4>";
        var content_html = "<p>"+content+"</p>"
        
        
        $.modal("<div>"+title_html+content_html+"</div>",
                {containerCss:{
                    height:height, 
                    width:width}})
 
    }


    function optionSelection(outputnetwork)
    {
        if(outputnetwork==113 || outputnetwork == 114 || outputnetwork==115)
        {
            showModalInfo("Warning: The generation of networks from Gene Ontology is time consuming.")
        }
    }

    function showModalLegend()
    {
        var title_html   = "<h4 class='modal'>Legend</h4>";
        var content_html = "<table cellspacing='10'>"+
                                "<tr>"+
                                    "<td><img src='./resources/img/tutorial/legend/genenode.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p>Gene node</p></td>"+
                                    "<td><img src='./resources/img/tutorial/legend/input_genenode.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p>Input gene node or related with one or more input genes (for network of genes from a input list of diseases).</p></td>"+
                                "</tr>"+
                                "<tr>"+
                                    "<td><img src='./resources/img/tutorial/legend/omimnode.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p>OMIM gene/disease node</p></td>"+
                                    "<td><img src='./resources/img/tutorial/legend/input_omimnode.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p>Input OMIM disease/gene node or related with one or more input disease (for network of diseases from a input list of genes).</p></td>"+
                                "</tr>"+
                                "<tr>"+
                                    "<td><img src='./resources/img/tutorial/legend/orphanode_lp.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p>Orphanet disease node (prevalence >1/1000)</p></td>"+
                                    "<td><img src='./resources/img/tutorial/legend/input_orphanode_lp.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p>Input Orphanet disease (prevalence >1/1000) or related with one or more input genes (for network of genes from a input list of diseases).</p></td>"+
                                "</tr>"+
                                "<tr>"+
                                    "<td><img src='./resources/img/tutorial/legend/orphanode.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p>Orphanet disease node (low prevalence <1/1000)</p></td>"+
                                    "<td><img src='./resources/img/tutorial/legend/input_orphanode.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p>Input Orphanet disease (low prevalence <1/1000) or related with one or more input disease (for network of genes from a input list of diseases).</p></td>"+
                                "</tr>"+   
                                "<tr>"+
                                    "<td><img src='./resources/img/profile.png' style='width:30px; height: 30px'></img></td>"+
                                    "<td> <p> Query node representing a set of HPO terms in networks created from a set of phenotypes.</p></td>"+
                                    "<td> </td>"+
                                    "<td> </td>"+
                                "</tr>"+ 
                            "</table>"
        
        $.modal("<div>"+title_html+content_html+"</div>",
                    {containerCss:{
                        height:600, 
                        width:700}
                    }
               )
    }

    