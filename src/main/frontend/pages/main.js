import 'jstree';
import 'datatables.net';
import 'datatables.net-dt/css/jquery.dataTables.css';
import 'jstree/dist/themes/default/style.css';
import './main.css';
import { Chart }  from 'chart.js';

export default function (options) {
    const page = new Page(options.el, options.component.key);
    page.init();
    return function() {
        el.innerHTML = "";
    };
};

const metrics = {
    selectors_key_selectors: { renderer: addKeySelectorUi, caption: 'Key Selectors' },
    specificity_values: { renderer: addSpecificityGraphUi, caption: 'Specificity Graph' },
    declarations_important: { renderer:  addDeclarationsImportantUi, caption: 'Declarations marked with !important' },
    declarations_properties: { renderer: addDeclarationsPropertiesUi, caption: 'Properties' },
    declarations_resets: { renderer: addDeclarationsResetsUi, caption: 'Reset Properties' },
    media_queries_values: { renderer: addMediaQueriesUi, caption: 'Media Queries' }
};

const metricGroups = {
    SELECTORS: [ 'selectors_key_selectors', 'specificity_values' ].join(),
    DECLARATIONS: [ 'declarations_important', 'declarations_properties', 'declarations_resets' ].join(),
    MEDIA_QUERIES: [ 'media_queries_values' ].join()
};

function toCaption(metricGroup) {
    switch (metricGroup) {
        case 'SELECTORS':
            return 'Selectors';
        case 'DECLARATIONS':
            return 'Declarations';
        case 'MEDIA_QUERIES':
            return 'Media Queries';
    }
    throw new Error('toCaption switch has to handle all possible values');
};

function addUiForMeasure(measure, container) {
    const metric = metrics[measure.metric];
    $(container).append(`<h2>${metric.caption}</h2><hr>`);
    const values = JSON.parse(measure.value);
    return metric.renderer(values, container);
}

function addKeySelectorUi(vals, container) {
    const table = document.createElement('table');
    container.appendChild(table);
    table.classList = ['display'];
    const data = [];
    for (let key of Object.keys(vals)) {
        data.push([ key, vals[key] ]);
    }
    $(table).DataTable({
        data,
        columns: [
            { title: 'Selector' },
            { title: 'Total' }
        ]
    });
}

function addSpecificityGraphUi(values, container) {
    const canvas = document.createElement('canvas');
    container.appendChild(canvas);
    const ctx = canvas.getContext('2d');
    const maxX = values.length;
    const chart = new Chart(ctx, {
        type: 'line',
        data: {
            datasets: [ {
                label: 'Specificity',
                xAxisID: 'only-x-axis',
                yAxisID: 'only-y-axis',
                data: values.map((v,i) => ({ x: i, y: v.specificity }))
            } ]
        },
        options: {
            scales: {
                xAxes: [{
                    id: 'only-x-axis',
                    type: 'linear',
                    ticks: {
                        min: 0,
                        max: maxX
                    }
                }],
                yAxes: [{
                    id: 'only-y-axis',
                    type: 'linear',
                    ticks: {
                        min: 0
                    }
                }]
            },
            tooltips: {
                callbacks: {
                    label: function(tooltipItem, chart) {
                        return [ 
                            `Specificity: ${tooltipItem.yLabel}`,
                            `Selector: ${values[tooltipItem.index].selector}`
                            ];
                    }
                }
            }
        }
    });
}

function addDeclarationsImportantUi(values, container) {
    const table = document.createElement('table');
    container.appendChild(table);
    table.classList = ['display'];
    const data = [];
    for (let value of values) {
        data.push([ value.property, value.value]);
    }
    $(table).DataTable({
        data,
        columns: [
            { title: 'Property' },
            { title: 'Value' }
        ]
    });
}

function addDeclarationsPropertiesUi(data, container) {
    const propertyView = new PropertyView(data, container);
    propertyView.transformData();
    propertyView.initialiseView();
}

function addDeclarationsResetsUi(values, container) {
    const table = document.createElement('table');
    container.appendChild(table);
    table.classList= ['display'];
    const data = [];
    for (let value of Object.keys(values)) {
        data.push([ value, values[value] ]);
    }
    $(table).DataTable({
        data,
        columns: [
            { title: 'Property' },
            { title: 'Total' },
        ]
    });
}

function addMediaQueriesUi(values, container) {
    $(container).append($(`<ul>${values.map(v => `<li>${v}</li>`).join('')}`));
}
    
class Page {
    
    constructor(el, componentKey) {
        this.el = el;
        this.componentKey = componentKey;
        this.api = new Api(componentKey);
    }
    
    init() {
        this.tree = document.createElement('div');
        this.el.classList = [ 'flex-container' ];
        this.el.appendChild(this.tree);
        this.createTabs();
        $('ul.css-tabs li').click((e) => this.changeCurrentTab(e.currentTarget));
        this.api.getProjectTree()
            .then(data => {
                this.root = data[0];
                const jtree = $(this.tree);
                jtree.jstree({ core: { data, multiple: false } });
                jtree.on('select_node.jstree', () => this.update());
                jtree.on('loaded.jstree', () => this.update());
            });
    } 
    
    changeCurrentTab(newTab) {
        $('ul.css-tabs li').removeClass('css-current');
        $('.css-tab-content').removeClass('css-current');
        $(newTab).addClass('css-current');
        const id = $(newTab).attr('data-tab');
        $('#' + id).addClass('css-current');
        this.update();
    }
    
    update() {
        const selected = $(this.tree).jstree().get_selected(true);
        const metricGroup = $('.css-tab-link.css-current').attr('data-tab');
        const container = $('#' + metricGroup);
        this.api.getMeasure(this.api.createMeasureOptions(selected[0].original.key, metricGroups[metricGroup]))
            .then(result => this.renderMetrics(result, container));
    }
    
    renderMetrics(data, container) {
        container.empty();
        const measureCompare = function (a, b) {
            if (a.metric < b.metric) {
                return -1;
            } else if (a.metric > b.metric) {
                return 1;
            } 
            return 0;
        };
        for (let measure of data.component.measures.sort(measureCompare)) {
            if (!measure.value) {
                continue;
            }
            const childContainer = document.createElement('div');
            childContainer.classList = [ 'box' ];
            container.append(childContainer);
            addUiForMeasure(measure, childContainer);
        }
    }
    
    createTabs() {
        const $container = $('<div class="css-container">');
        const $tabs = $('<ul class="css-tabs">');
        $container.append($tabs);
        let counter = 0;
        for (let group of Object.keys(metricGroups)) {
            const $tab = $(`<li class="css-tab-link" data-tab="${group}">${toCaption(group)}</li>`);
            $tabs.append($tab);
            const $content = $(`<div id=${group} class="css-tab-content">`);
            if (counter === 0) {
                $tab.addClass('css-current');
                $content.addClass('css-current');
            }
            $container.append($content);
            counter++;
        }
        $(this.el).append($container);
    }
}

function toJsTreeJson(sonarJson) {
    const root = {
        id: sonarJson.baseComponent.id,
        parent: '#',
        text: sonarJson.baseComponent.name,
        state: { opened: true, selected: true },
        key: sonarJson.baseComponent.key
    };
    const dirs = [];
    const files = [];
    for (let component of sonarJson.components) {
        let parentId;
        if (component.qualifier === 'DIR') {
            dirs.push({ 
                id: component.id,
                parent: root.id,
                text: component.name,
                key: component.key
            });
        }
        if (component.qualifier === 'FIL') {
            files.push({
                id: component.id,
                parent: dirs[dirs.length - 1].id,
                text: component.name,
                icon: 'jstree-file',
                key: component.key
            });
        }
    }
    return [ root ].concat(dirs).concat(files); 
}

class Api {
    constructor(componentKey) {
        this.componentKey = componentKey;
        this.routes = Object.freeze({
            COMPONENTS_TREE: 'components/tree',
            MEASURE: 'measures/component'
        });
    }

    prefix(route) {
        return '/api/' + route;
    }
    
    getProjectTree() {
        return window.SonarRequest
            .getJSON(this.prefix(this.routes.COMPONENTS_TREE), { component: this.componentKey, s: 'path' })
            .then(result => toJsTreeJson(result));
    }
    
    getMeasure(options) {
        return window.SonarRequest
            .getJSON(this.prefix(this.routes.MEASURE), options);
    }
    
    createMeasureOptions(componentKey, metricKeys) {
        return {
            componentKey,
            metricKeys
        };
    }
}

class PropertyView {

    constructor(data, container) {
        this.data = data;
        this.container = container;
        this.properties = {};
        this.currentProperty = '';
        this.dataTableInitialized = false;
    }
    
    transformData() {
        for (let property of Object.keys(this.data)) {
            const values = this.data[property];
            const dict = {};
            for (let value of values) {
                if (dict[value]) {
                    dict[value]++;
                } else {
                    dict[value] = 1;
                }
            }
            this.properties[property] = dict;
        }
    }
    
    initialiseView() {
        this.$search = $('<input type="text" placeholder="search"/>');
        this.$search.on('input', e => this.searchEntered(e.target.value));
        this.$list = $(`<select multiple size="10" class="css-propertyList">${Object.keys(this.properties).sort().map((p,i) => this.createItem(p, i)).join('')}</select>`);
        this.$list.change(() => this.selected());
        this.$table = $('<table>');
        const chooserTableContainer = $('<div>');
        chooserTableContainer.addClass('flex-container')
            .append([
                $('<div>').addClass('flex-container vertical').append([ this.$search, this.$list ]), 
                this.$table 
            ]);
        $(this.container).append(chooserTableContainer);
    }
    
    createItem(property, index) {
        return `<option>${property}</option>`;
    }
    
    selected() {
        const selected = this.$list.children('option:selected');
        if (selected.length === 0) {
            return;
        }
        // Property has to be always included, it is not possible for dataTables to dynamically add columns (1.10)
        const columns = [
            { title: 'Property' },
            { title: 'Value' },
            { title: 'Total' },
        ];
        const data = [];
        for (let option of selected) {
            const values = this.properties[option.text];
            for (let value of Object.keys(values)) {
                const row = [ option.text, value, values[value] ];
                data.push(row);
            }
        }
        if (this.dataTableInitialized) {
            const api = this.$table.dataTable().api();
            api.clear();
            api.rows.add(data);
            api.draw();
        } else {
            this.$table.dataTable({
                data,
                columns
            });
            this.dataTableInitialized = true;
        }
    }
    
    clickedItem(property) {
        console.log(property);
    }
    
    searchEntered(searchTerm) {
        this.$list.children().each(function(){
            const notMatched = $(this).text().indexOf(searchTerm) === -1;
            $(this).toggleClass('hidden', notMatched);
        });
    }
    
}
