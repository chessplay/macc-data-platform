const imgConfig = {
	AP10X:['RAP120(V2)', 'RAP100', 'EAP101', 'EAP102'],
	AP20X:['EAP201', 'EAP202'],
	AP21X:['RAP210(B)', 'RAP210(V2)', 'RAP210(EV2)'],
	AP22X:['RAP220(V2)', 'RAP220(EV2)', 'RAP230'],
	AP602:['EAP602']
};
let devImg = {}
for(let key in imgConfig) {
	imgConfig[key].forEach(item => {
		devImg[item] = key
	});
} 
devImg.default = 'AP';
export default devImg