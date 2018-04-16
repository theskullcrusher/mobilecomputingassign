package com.group5project.mobilecomputing.group5;

/**
 * Created by srinija on 3/31/18.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.group5project.mobilecomputing.group5.R;

public class Tab3 extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3, container, false);
        WebView myWebView;
        myWebView = (WebView)rootView.findViewById(R.id.myWebView);

        String customHtml = "<html>\n" +
                "<head>\n" +
                " <meta charset=\"UTF-8\">\n" +
                "<!-- Plotly.js -->\n" +
                "<script src=\"https://cdn.plot.ly/plotly-latest.min.js\"></script>\n" +
                "<script src=\"https://d3js.org/d3.v5.min.js\"></script>\n" +
                "<script>\n" +
                "var alldata = [];\n" +
                "var all_traces = [];\n" +
                "var  colors = ['#1f77b4','#9467bd','#bcbd22', '#FFFF00'];\n" +
                "\n" +
                "function loadata(){\n" +
                "\t\n" +
                " \tPlotly.d3.csv(\"https://raw.githubusercontent.com/srinijakanteti/csvfiles/master/activitydb.csv\", function(data) {\n" +
                "        \talldata = data;\n" +
                "        \t\n" +
                "    });\n" +
                " }\n" +
                "\n" +
                "\n" +
                "\n" +
                "window.onload = loadata();\n" +
                "</script>\n" +
                "\n" +
                "\n" +
                "</head>\n" +
                "<body>\n" +
                "<!-- Plotly chart will be drawn inside this DIV -->\n" +
                "<p id=\"print\"></p>\n" +
                "<p id=\"print1\"></p>\n" +
                "<p id=\"print2\"></p>\n"+
                "<div id=\"boxdiv\">\n" +
                "\t<input type=\"checkbox\" value=1 onclick=senddata() name=\"activity\">Walking\n" +
                "\t<input type=\"checkbox\" value=2 onclick=senddata() name=\"activity\">Running\n" +
                "\t<input type=\"checkbox\" value=4 onclick=senddata() name=\"activity\">Jumping<br> \n" +
                "</div>\n" +
                "<div id=\"graphdiv\" style=\"width:auto; height:auto\"></div>\n" +
                "\n" +
                "<script type=\"text/javascript\">\n" +
                "\t\n" +
                "function single_activity(graphdata, choice) {\n" +
                "\t// console.log(graphdata)\n" +
                "\tfor (var j = 0;j<graphdata.length;j++){\n" +
                "\t\tcurrentActivity = graphdata[j];\n" +
                "\t\t\n" +
                "\t\tfor (var k=0;k<currentActivity.length;k++){\n" +
                "\t\t\tdataX = [];\n" +
                "\t\t\tdataY = [];\n" +
                "\t\t\tdataZ = [];\n" +
                "\n" +
                "\t\t\tif(k == 0 || k == 20 || k == 40){legendvalue = true;}\n" +
                "\t    \telse{legendvalue = false;}\n" +
                "\n" +
                "\t\t\tfor(var i=1;i<=50;i++){\n" +
                "\t\t\t\ttemp1 = 'AccelX'+(i).toString()\n" +
                "\t\t\t\ttemp2 = 'AccelY'+(i).toString()\n" +
                "\t\t\t\ttemp3 = 'AccelZ'+(i).toString()\n" +
                "\n" +
                "\t\t\t\tdataX.push(currentActivity[k][temp1]);\n" +
                "\t\t\t\tdataY.push(currentActivity[k][temp2]);\n" +
                "\t\t\t\tdataZ.push(currentActivity[k][temp3]);\n" +
                "\n" +
                "\t\t\t\ttrace = {\n" +
                "\t                x: dataX,\n" +
                "\t                y: dataY,\n" +
                "\t                z: dataZ,\n" +
                "\t                showlegend: legendvalue,\n" +
                "\t                name: currentActivity[0]['Activity'],\n" +
                "\n" +
                "\t                mode: 'lines',\n" +
                "\t                marker: {\n" +
                "\t                \tcolor: colors[j],\n" +
                "\t                \tsize: 12,\n" +
                "\t                \tsymbol: 'circle',\n" +
                "\t                \tline: {\n" +
                "\t                  \t\tcolor: 'rgb(0,0,0)',\n" +
                "\t                  \t\twidth: 0\n" +
                "\t                \t}\n" +
                "\t                },\n" +
                "\t                line: {\n" +
                "\t                \tcolor: colors[j],\n" +
                "\t                \twidth: 1\n" +
                "\t                },\n" +
                "\t                type: 'scatter3d'\n" +
                "\t        \t};\n" +
                "\t        }\n" +
                "\t\t\tall_traces.push(trace);\n" +
                "\t\t\t// console.log(all_traces[0]);\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\treturn all_traces;\n" +
                "}\n" +
                "\n" +
                "\t\n" +
                "function makegraph(choice)\n" +
                "{\n" +
                "\tgraphdata = []\n" +
                "\tif (choice == 0)\n" +
                "\t{\n" +
                "\t\tall_traces = [];\n" +
                "\t}\n" +
                "\tif (choice == 1)\n" +
                "\t{\n" +
                "\t\tall_traces = [];\n" +
                "\t\tgraphdata.push(alldata.slice(0,20));\n" +
                "\t\tall_traces = single_activity(graphdata, choice);\n" +
                "\t}\n" +
                "\t \n" +
                "\tif (choice == 2)\n" +
                "\t{\n" +
                "\t\tall_traces = [];\n" +
                "\t\tgraphdata.push(alldata.slice(20,40));\n" +
                "\t\tall_traces = single_activity(graphdata, choice);\n" +
                "\t}\n" +
                "\n" +
                "\tif (choice == 4)\n" +
                "\t{\n" +
                "\t\tall_traces = [];\n" +
                "\t\tgraphdata.push(alldata.slice(40,60));\n" +
                "\t\tall_traces = single_activity(graphdata, choice);\n" +
                "\t}\n" +
                "\n" +
                "\n" +
                "\tif (choice == 3)\n" +
                "\t{\n" +
                "\t\tall_traces = [];\n" +
                "\t\tgraphdata.push(alldata.slice(0,20));\n" +
                "\t\tgraphdata.push(alldata.slice(20,40));\n" +
                "\t\tall_traces = single_activity(graphdata, choice);\n" +
                "\t}\n" +
                "\n" +
                "\n" +
                "\tif (choice == 6)\n" +
                "\t{\n" +
                "\t\tall_traces = [];\n" +
                "\t\tgraphdata.push(alldata.slice(20,40));\n" +
                "\t\tgraphdata.push(alldata.slice(40,60));\n" +
                "\t\tall_traces = single_activity(graphdata, choice);\n" +
                "\t}\n" +
                "\n" +
                "\tif (choice == 5)\n" +
                "\t{\n" +
                "\t\tall_traces = [];\n" +
                "\t\tgraphdata.push(alldata.slice(0,20));\n" +
                "\t\tgraphdata.push(alldata.slice(40,60));\n" +
                "\t\tall_traces = single_activity(graphdata, choice);\n" +
                "\t}\n" +
                "\n" +
                "\tif (choice == 7)\n" +
                "\t{\n" +
                "\t\tall_traces = [];\n" +
                "\t\tgraphdata.push(alldata.slice(0,20));\n" +
                "\t\tgraphdata.push(alldata.slice(20,40));\n" +
                "\t\tgraphdata.push(alldata.slice(40,60));\n" +
                "\t\tall_traces = single_activity(graphdata, choice);\n" +
                "\t}\t\n" +
                "\t// console.log(all_traces.length)\n" +
                "\tvar data = all_traces;\n" +
                "\tvar layout = {\n" +
                "\t\n" +
                "\t\t\ttitle: 'Activities',\n" +
                "\t\t\tautosize: false,\n" +
                "\t\t\twidth: 500,\n" +
                "\t\t\theight: 500,\n" +
                "\t\t\tmargin: {\n" +
                "\t\t\tl: 0,\n" +
                "\t\t\tr: 0,\n" +
                "\t\t\tb: 0,\n" +
                "\t\t\tt: 65\n" +
                "\t\t\t}\n" +
                "\t};\n" +
                "\tPlotly.purge('graphdiv')\n" +
                "\tPlotly.newPlot('graphdiv', data, layout);\n" +
                "}\n" +
                "\n" +
                "\n" +
                "setTimeout(senddata, 4000);\n" +
                "\n" +
                "function senddata() {\n" +
                "\tsum = 0\t\n" +
                "\tvar inputElements = document.getElementsByName(\"activity\");\n" +
                "\tfor(var i=0; i<inputElements.length; i++){\n" +
                "\t\tif(inputElements[i].checked){\n" +
                "           sum += parseInt(inputElements[i].value);\n" +
                "       } \n" +
                "\t}\n" +
                "// console.log(alldata.length);\n" +
                "\n" +
                "makegraph(sum);\n" +
                "}\n" +
                "\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadData(customHtml, "text/html", "UTF-8");
        return rootView;
    }
}
