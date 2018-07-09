let ProgramTemplate = {
    info: {
        name: 'program-manage', //注册组件名
        template_url: '/components/template/program-manage.view.html', //模块 页面模板 url
        author: 'Wangch',
        descript: '节目管理'
    },
    data() {
        return {
            //加载进度条
            loading: true,
            curCate: {
                name: '所有分类',
                pkId: 0
            },
            catelogTreeData: [],
            defaultProps: {
                children: 'children',
                label(data, node) {
                    return data.data.name;
                }
            },
            programTemplates: [],
            tpager: {
                total: 0,
                current: 1,
                size: 10
            }
        };
    },
    mounted() {
        this.loadCategory();
        this.loadCategoryProgram();
    },
    methods: {
        //选择类别目录
        selectCatelog(data) {
            if (this.loading) return;
            this.curCate = data.data;
            this.loadCategoryProgram();
        },
        //加载类别信息
        loadCategory() {
            let url = '/program/category';
            ajax_promise(url, 'get').then((result) => {
                this.catelogTreeData = result.data;
            });
        },
        //加载类别节目
        loadCategoryProgram() {
            this.loading = true;
            let url = '/program/' + this.curCate.pkId + '/programs/' + this.tpager.current + '-' + this.tpager.size;
            ajax_promise(url, 'get').then(result => {
                this.loading = false;
                this.tpager.total = result.pager.total;
                this.programTemplates = result.data;
            });
        },
        //打开节目预览
        openProgramView(pt) {
            let url = '/sola/view/' + pt.programId;
            //window.open(url, "_blank", "width=800 height=600");
            openwindow(url,'节目预览',1024,768);
        },
        handleSizeChange(val) {
            this.tpager.size = val;
            this.loadCategoryProgram();
        },
        handleCurrentChange(val) {
            this.tpager.current = val;
            this.loadCategoryProgram();
        },
        editProgram(pt) {
            let url = '/sola/edit/' + pt.programId;
            window.open(url, "_blank", "width="+screen.availWidth+" height="+screen.availHeight);
        },
        pubProgram(p) {
            //发布节目
            this.$emit('publish', p);
        }

    }
}

export default ProgramTemplate;