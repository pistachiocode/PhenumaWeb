    

//globals
var current_vis;

var current_network;
var current_subnetworks;

var initialFilters;
var labels_visible = false;

//relationshiptype
var simsemhpo = 0
var simsemgobp = 1
var simsemgocc = 2
var simsemgomf = 3
var infomim = 4
var inforpha = 5
var infgene = 6
var ppi = 7
var metabolic = 8
var omim = 9
var orphadata = 10
var queryer = 11
var querydisease = 12
var querygene =  13

//node types
var disease_type = 2;
var gene_type = 1;
var orphanum_type = 3;
var orphanum_low_1_type = 4;
var orphanum_low_2_type = 5;
var orphanum_low_3_type = 6;
var orphanum_low_4_type = 7;
var query_type = 0;


var filteredEnriched = false;
var filteredNew = false;


//events information
var lastevent;
var lastevent_target;

var clickposx;
var clickposy;

function cytoscape(div_id, net, subnetworks){

    // initialization options
    var options = {
        // where you have the Cytoscape Web SWF
        swfPath: "./resources/cytoscapeweb/swf/CytoscapeWeb",
        // where you have the Flash installer SWF
        flashInstallerPath: "./resources/cytoscapeweb/swf/playerProductInstall"
    };

    var visual_style = getDarkVisualStyle();
    
    // init and draw
    current_vis = new org.cytoscapeweb.Visualization(div_id, options);
    
    
    current_vis.addListener("select", "nodes", function(evt) {
           
        handleNodeSelect(evt);
       
    }).addListener("select", "edges", function(evt) {

        var edge = evt.target;    
        edgeTable(edge);  
        
        handleEdgeSelect(evt);
        
    }).addListener("click", "edges", function(evt) {
        
        clickposx = evt.mouseX;
        clickposy = evt.mouseY;
        
    });
    
    current_vis.addListener("dblclick", "nodes", function(evt) {
        
        var node = evt.target;  
        var fNeighbors = current_vis.firstNeighbors([node]);
        var neighborNodes = fNeighbors.neighbors;
        var edges = fNeighbors.edges;
        current_vis.select([node]).select(neighborNodes).select(edges);

    }).addListener("click", "none", function(evt) {
        
        destroyQtip();
        
    });


    //destroy previous qtips
   // destroyQtip();
   
    current_vis.draw({network: net, 	
                      visualStyle: visual_style});
    
    
   
    current_network = net;      

    current_subnetworks   = subnetworks;

    current_vis.ready(function () {
        initializeOptions();
        initializeFilters();
        setOptions();
        setFilters();
  
    });
    
    
}

/*Network Events*/

function handleNodeSelect(event){
    destroyQtip();
    
    var node = event.target;  
    nodeTable(node);  

    getNodeInformation(event.target[0].data.type+":"+event.target[0].data.id)
    viewEnrichment(current_vis.selected("nodes"))
    lastevent = event;
    lastevent_target = "node";

}

function handleEdgeSelect(event){
    
   // getEdgeInformation(event.target[0].data.id)
    
    lastevent = event;
    lastevent_target = "edge";
}


//Returs the object with the last event executed to show the qtip.
function getLastEvent()
{
    return lastevent;
}


//Returs a string with the target of the last event: edge or node.
function getLastEventTarget()
{
    return lastevent_target;
}


function getClickPosx()
{
    return clickposx;
}

function getClickPosy()
{
    return clickposy;
}




/* ------- Variables query methods -------- */

function getNetwork()
{
    return  current_network;
}


function getSubnetworks()
{
    return current_subnetworks;
}


/* -------  Filters ---------*/

/**
 * show/hide enriched nodes
 */

function filterEnriched(nodos){

    current_vis.filter("nodes", nodos);
    filteredEnriched = true;
}

function removeFilterEnriched(){
    
    if(filteredEnriched)
    {
        current_vis.removeFilter("nodes", true);
        hideIsolatedNodes();
        filteredEnriched = false;
    }
}

/*
 * show/hide new relationships 
 */

function filterNew(){
    filteredNew = !filteredNew;
    setFilters();
}

function filterRelationship(subnetwork){

    current_vis.filter("edges", function(edge){						
            return  edge.data.type == subnetwork;
    });
    hideIsolatedNodes(); 
}


/**
 * PhenUMA filters: type of relationships and score
 */
function removeFilterRelationship(){
    current_vis.removeFilter("edges", true);
    hideIsolatedNodes();
    setFilters();
}

function initializeOptions(){
    _initialOptions = {layout: 'Tree',
                       0 : true, 
                       1 : true,
                       2 : true,
                       3 : true,
                       4 : true,
                       5 : true,
                       6 : true,
                       7 : true,
                       8 : true,
                       9 : true,
                       10 : true,
                       11 :true,
                       12 : true,
                       13 : true};
}

function initializeFilters(){
    initialFilters = {0: 0,
                      1: 0,
                      2: 0,
                      3: 0,
                      4 : 0,
                      5 : 0,
                      6 : 0,
                      7 :0};
}

function setOptions()
{
        for(var i = 0; i<=13 ; i++)
        {
            //Celullar Component
            if(_initialOptions[i])
                    _initialOptions[i] = addElements(current_subnetworks[i]);
            else
                    _initialOptions[i] = !removeElements(current_subnetworks[i]);
        }

        setLayout("ForceDirected");        
        
    	hideIsolatedNodes()
    	
}

function setFilters()
{
    
   if(!filteredNew)
   {
        current_vis.filter("edges", function(edge){						
                return (edge.data.score >= initialFilters[simsemgocc] && edge.data.type == simsemgocc) ||  /*scored relationships*/
                       (edge.data.score >= initialFilters[simsemgobp] && edge.data.type == simsemgobp) || 
                       (edge.data.score >= initialFilters[simsemgomf] && edge.data.type == simsemgomf) || 
                       (edge.data.score >= initialFilters[simsemhpo] && edge.data.type == simsemhpo) || 
                       (edge.data.score >= initialFilters[infgene] && edge.data.type == infgene) ||
                       (edge.data.score >= initialFilters[infomim] && edge.data.type == infomim) || 
                       (edge.data.score >= initialFilters[inforpha] && edge.data.type == inforpha) ||
                       (edge.data.type == ppi || /*not scored relationships*/
                        edge.data.type == metabolic || 
                        edge.data.type == omim ||
                        edge.data.type == orphadata ||
                        edge.data.type == querydisease ||
                        edge.data.type == querygene ||
                        edge.data.type == queryer );
        }, true);

   }
   else
   {
        current_vis.filter("edges", function(edge){						
            return  edge.data.isnew && edge.data.type == simsemhpo && edge.data.score >= initialFilters[simsemhpo];
        }, true);
        
   }
   
   hideIsolatedNodes(); 
}


function addElements(subnetwork){
   // alert(subnetwork)
    if(subnetwork!=null){
            current_vis.addElements(subnetwork, true);
            return true
    }
    return false;
}

function removeElements(subnetwork){
    if(subnetwork!=null){
            current_vis.removeElements(subnetwork, true);
            return true;
    }
    return false;
}

function showHideSubnetworks(subnetwork)
{
    if(!_initialOptions[subnetwork])
        _initialOptions[subnetwork] = addElements(current_subnetworks[subnetwork]);
    else
        _initialOptions[subnetwork] = !removeElements(current_subnetworks[subnetwork]);

    setFilters()
    
    $("div[id ='loadingimg']").hide()
}


function hideIsolatedNodes(){
    current_vis.removeFilter("nodes", true);

    current_vis.filter("nodes", function(node){
        var fn = current_vis.firstNeighbors([node], true);
        return fn.neighbors.length > 0;
    }, true);
}

/*
 * filtering edges by score
 */
function applyFilter(score, type){
    initialFilters[type] = score
    setFilters();
}


/* ========= SELECTION EVENTS =========== */


/*
 * Node selection
 */
function selectNode(id){
    current_vis.deselect("nodes")
    current_vis.select("nodes",[id])
}

function selectEdge(id){  
    current_vis.deselect("edges")
    current_vis.select("edges",[id])
}


/* ======== TOOLBAR ENVENTS ========== */

/** 
 * Zoom in - Zoom out
 */
function zoomIn()
{
    var zoom = current_vis.zoom();
    current_vis.zoom(zoom+0.1);
}

function zoomOut()
{
    var zoom = current_vis.zoom();
    current_vis.zoom(zoom-0.1)
   //current_vis.exportNetwork('xgmml', 'export.php?type=xml');
}

function panEnabled()
{   
    var enabled = current_vis.panEnabled()
    current_vis.panEnabled(!enabled);
}

function zoomToFit()
{   
    current_vis.zoomToFit();
}

function toggleLabels(){
    labels_visible = !labels_visible
    current_vis.nodeLabelsVisible(labels_visible)
}

function setLayout(layout){
    
   // current_vis.layout({name:layout, options:options})
    current_vis.layout(layout);
}

function panToCenter()
{
    //current_vis.panToCenter()
}

function getXGMML(){
    window.open('data:application/xml;charset=utf-8,' +encodeURIComponent(current_vis.xgmml()));
}

function getPNG(){
    window.open('data:image/png;base64,' +encodeURIComponent(current_vis.png()));
}

function getPDF(){
    document.location = 'data:image/pdf;base64,' +encodeURIComponent(current_vis.pdf());
}


/* ========== STYLES ===========*/

function getDarkVisualStyle()
{
    var visual_style = {
        global: {
          backgroundColor: "#393A3A"
        },
        nodes: {
            opacity: 1,
            selectionColor: "#FF0000",
            selectionGlowColor: "#FFFFFF",
            selectionBorderColor: "#686868",
            borderWidth: 3,
            borderColor:{ 
                discreteMapper: {
                    attrName: "input",
                    entries: [
                            {attrValue: 1, value: "#900000"},
                            {attrValue: 0, value: "#393A3A"},
                    ]
                }
            }, 
            labelFontColor: "#FFFFFF",
            labelFontSize: 12,
            size: {
                discreteMapper: {
                    attrName: "input",
                    entries: [
                            {attrValue: 0, value: 20},
                            {attrValue: 1, value: 25}
                    ]
                }
            },
            image: {
                discreteMapper: {
                    attrName: "type",
                    entries: [
                            {attrValue: 0, value: "./resources/img/profile.png"}
                    ]
                }
            },
            color: {
                discreteMapper: {
                    attrName: "type",
                    entries: [
                            {attrValue: gene_type, value: "#F78181"},
                            {attrValue: disease_type, value: "#F4FA58"},
                            {attrValue: orphanum_type, value: "#FFFFFF"},
                            {attrValue: orphanum_low_1_type, value: "#B8D9F8"},
                            {attrValue: orphanum_low_2_type, value: "#B8D9F8"},
                            {attrValue: orphanum_low_3_type, value: "#B8D9F8"},
                            {attrValue: orphanum_low_4_type, value: "#B8D9F8"},
                            {attrValue: query_type, value: "#FF9933"}
                    ]
                }
            },  
            shape: {
                discreteMapper: {
                    attrName: "type",
                    entries: [
                            {attrValue: gene_type, value: "TRIANGLE"},
                            {attrValue: disease_type, value: "ELLIPSE"},
                            {attrValue: orphanum_type, value: "OCTAGON"},
                            {attrValue: orphanum_low_1_type, value: "OCTAGON"},
                            {attrValue: orphanum_low_2_type, value: "OCTAGON"},
                            {attrValue: orphanum_low_3_type, value: "OCTAGON"},
                            {attrValue: orphanum_low_4_type, value: "OCTAGON"},
                    ]
                }
            },
            labelVerticalAnchor: "top"

        },
        edges:{    
            selectionColor: "#FF0000",
            selectionGlowColor: "#CCFF00",
            selectionBorderColor: "#686868",
            width: {
                defaultValue: 0,
                continuousMapper: {attrName: "score", minValue: 1, maxValue: 5}
            },
            color: {
                discreteMapper: {
                    attrName: "type",
                    entries: [
                            {attrValue: simsemhpo, value: "#9FF781"}, //green #9FF781
                            {attrValue: simsemgobp, value: "#A9A9F5"}, // A9A9F5
                            {attrValue: simsemgocc, value: "#0040FF"}, //9FF781/ 005df6
                            {attrValue: simsemgomf, value: "#CEF6F5"}, // CEF6F5
                            {attrValue: infgene, value: "#F7D358"}, 
                            {attrValue: infomim, value: "#d4ff2a"}, 
                            {attrValue: inforpha, value: "#ffaaaa"}, 
                            {attrValue: ppi, value: "#FAAC58"}, 
                            {attrValue: metabolic, value: "#FF00BF"}, //pink
                            {attrValue: omim, value: "#ffdd55"}, 
                            {attrValue: orphadata, value: "#ffaaaa"}, 
                            {attrValue: querydisease, value: "#9FF781"}, //green
                            {attrValue: querygene, value: "#9FF781"}, //green
                            {attrValue: queryer, value: "#9FF781"}  //green
                            
                    ]
                }    
            },
            style: {
                discreteMapper: {
                    attrName: "isnew",
                    entries: [ {attrValue: true, value: "SOLID"},
                               {attrValue: false, value: "SOLID"}] 
                }    
            }
            
        }


    };
    
    return visual_style;
    
    
}

function relationshipInfo(type)
{
    return   "<p><b>Score:</b>${score}</p>"+
             "<p><b>From:</b>${source}</p>"+
             "<p><b>To:</b>${target}</p>"+
             "<p><b>Type:</b>" +getRelationshipType(type)+"</p>"
    
} 

/**
 * Tables
 */

function nodeTable(nodes)
{

    var tableInfo = $("#nodeTable").html()
    
    tableInfo = "<thead><tr><th></th>"+
                "<th><a onclick=\"SortTable(1, \'nodeTable\', 'id', 'number');\" href='javascript:;'>ID</a></th>"+
                "<th><a onclick=\"SortTable(2, \'nodeTable\', 'name', text');\" href='javascript:;'>Name/Symbol</a></th>"+
             //   "<th><a onclick=\"SortTable(3, \'nodeTable\', 'text');\" href='javascript:;'>Prevalence</a></th>"+
                "<th>Link</th>"+
                "</tr></thead><tbody>";
            

    
    var i = 0;
    
    while(i<nodes.length){
        
        var nodetype = ""
        if (nodes[i].data.type == 1)
        {
            nodetype = "phenumageneicon"
        }
        else if (nodes[i].data.type == 2)
        {
            nodetype = "phenumadiseaseicon"
        }
        else if (nodes[i].data.type >= 3)
        {
            nodetype = "phenumaorphaneticon"
        }
            
        tableInfo = tableInfo +"<tr class='phenumarow' onclick='viewPhenotypes(\""+nodes[i].data.type+":"+nodes[i].data.id+"\")'>"+
                           "<td class='"+nodetype+"' style='background-color:white'></td>"+
                               "<td class='phenumadata'>"+nodes[i].data.id+"</td>"+
                               "<td class='phenumadata_text'>"+nodes[i].data.label+"</td>"+
                            //   "<td class='phenumadata_text'>"+getPrevalence(nodes[i].data.type)+"</td>"+
                               "<td class='phenumadata'><a href=\""+getLink(nodes[i].data, nodes[i].data.type)+"\" target=\"_blank\"><img src=\"./resources/img/world_link.png\"/></a></td>"+
                               "</tr>";

        i = i+1;
        
    }
    
    tableInfo = tableInfo + "</tbody>";
    
    
    $("#nodeTable").html(tableInfo)
    
}

function viewPhenotypes(node){
    $("input[name = 'falseform:selectedNode']").val(node)
    $("a[id = 'falseform:button']").click()
}

function viewRelationships(edge){
    $("input[name = 'falseform:selectedEdge']").val(edge)
    $("a[id = 'falseform:buttonedge']").click()
}

function viewEnrichment(nodes){
    var nodesids = "";
    
    var i = 0;
    while(i<nodes.length){
        nodesids = nodesids + nodes[i].data.id + ",";
        i = i+1;
    }
    
    $("input[name = 'falseform:selectedNodes']").val(nodesids)
    $("a[id = 'falseform:buttonenrichmentsubmit']").click()
}

function exitsQueryRelationship(edges)
{
    var i = 0;
    
    while(i<edges.length && !(edges[i].data.source == "0" || edges[i].data.target == "0") )
    {    
        i=i+1;
    }
    
    return (i<edges.length);
        
}

/**
 * Create HTML table of selected edges. The phenotype query relationships have more fields so we
 * have to check if is necesary show it.
 */
function edgeTable(edges)
{
    var tableInfo = $("#edgeTable").html()
    
    
    
    
    tableInfo = "<thead><tr class='phenumarow'><th></th>"+
                "<th><a onclick=\"SortTable(1, 'edgeTable', 'source', 'number');\" href='javascript:;'>Source</a></th>"+
                "<th><a onclick=\"SortTable(2, 'edgeTable', 'target', 'number');\" href='javascript:;'>Target</a></th>"+
                "<th><a onclick=\"SortTable(3, 'edgeTable', 'score', 'float');\" href='javascript:;'>Score</a></th>";
    
    /**
     * Chech if pvalues are included
     */
    var query_relationship = exitsQueryRelationship(edges);  
    
    if(query_relationship){
        tableInfo = tableInfo +  "<th><a onclick=\"SortTable(4, 'edgeTable', 'float');\" href='javascript:;'>PValue</a></th>"            
                              +  "<th><a onclick=\"SortTable(5, 'edgeTable', 'text');\" href='javascript:;'>Type</a></th>"
                              +  "</tr></thead><tbody>";
    }
    else
    {
        tableInfo = tableInfo +  "<th><a onclick=\"SortTable(4, 'edgeTable', 'text');\" href='javascript:;'>Type</a></th>"
                              +  "</tr></thead><tbody>";    
    }
    
    
    var pvalue = "-";
    var i = 0;
    while(i<edges.length){
        
        var type  = ""
        var color = "#FFF"
        var score = edges[i].data.score
        if (edges[i].data.type == simsemhpo)
        {
            type = "Phenotypic"
            color = "#9FF781"
            
            if(query_relationship)
                pvalue = edges[i].data.pvalue;
        }
        else if (edges[i].data.type == simsemgobp)
        {
            type = "Functional (Biological Process)"
            color = "#A9A9F5"
        }
        else if (edges[i].data.type == simsemgocc)
        {
            type = "Functional (Cellular Component)"
            color = "#005df6"
        }
        else if (edges[i].data.type == simsemgomf)
        {
            type = "Functional (Molecular Function)"
            color = "#CEF6F5"
        }
        else if (edges[i].data.type == ppi)
        {
            type = "PPI"
            color = "#FAAC58"
        }
        else if (edges[i].data.type == metabolic)
        {
            type = "Metabolic"        
            color = "#FF00BF"
            score = "-"
        }
        else if (edges[i].data.type == omim)
        {
            type = "Omim"   
            color = "#ffdd55"
            score = "-"
        }
        else if (edges[i].data.type == orphadata)
        {
            type = "Orphanet"   
            color = "#ffaaaa"
            score = "-"
        }
        else if (edges[i].data.type == infgene)
        {
            type = "Inferred from Genes"        
            color = "#F7D358"
            //Multiplicar por 10 en la tabla lateral.
            score = score * 10;
        }
        else if (edges[i].data.type == infomim)
        {
            type = "Inferred from Omim"   
             color = "#d4ff2a"
             //Multiplicar por 10 en la tabla lateral.
            score = score * 10;
        }
        else if (edges[i].data.type == inforpha){
            type = "Inferred from Orphan Diseases"  
             color = "#ffaaaa"
             //Multiplicar por 10 en la tabla lateral.
            score = score * 10;
        }
        
        
        
        tableInfo = tableInfo +"<tr class='phenumarow' onclick='viewRelationships(\""+edges[i].data.id+"\")'>"+
                               "<td style='background-color:"+color+"; width:10px;'>";
                           
        if(edges[i].data.isnew)
            tableInfo = tableInfo + "<div class='ui-icon ui-icon-star'></div></td>";
        else
            tableInfo = tableInfo + "<div class='ui-icon ui-icon-triangle-1-e'></div></td>";
        
        /* Change value 0 for 'Query' in queries of phenotypes */
        if(edges[i].data.source == "0")
            tableInfo = tableInfo + "<td class='phenumadata'>Query</td>";
        else
            tableInfo = tableInfo + "<td class='phenumadata'>"+edges[i].data.source+"</td>";
    
    
        if(edges[i].data.target == "0")
            tableInfo = tableInfo + "<td class='phenumadata'>Query</td>";
        else
            tableInfo = tableInfo + "<td class='phenumadata'>"+edges[i].data.target+"</td>";
    

        tableInfo = tableInfo +  "<td class='phenumadata'>"+score+"</td>";
        
        
        /* PValues in queries of phenotypes */
        if(query_relationship)
            tableInfo = tableInfo + "<td class='phenumadata'>"+pvalue+"</td>";
                               
                                
        tableInfo = tableInfo + "<td class='phenumadata'>"+type+"</td>"+
                                "</tr>";
                           
        i = i+1;
        
    }
    
    tableInfo = tableInfo + "</tbody>";
    
    
    $("#edgeTable").html(tableInfo)
    
}




