const SwitchTypeList = {
	"S1920-8GT2SFP": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, null, null, null, 'Gi9F', 'Gi10F']
		]
	},
	"S1920-8GT2SFP-P": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, null, null, null, 'Gi9F', 'Gi10F']
		]
	},
	"S1920-8T2GT/2SFP": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, 'Gi9', 'Gi10', null, 'Gi9F', 'Gi10F']
		]
	},
	"S1920-8T2GT/2SFP-P": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, 'Gi9', 'Gi10', null, 'Gi9F', 'Gi10F']
		]
	},
	"S1920-16T2GT2SFP": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', null, 'Fa9', 'Fa11', 'Fa13', 'Fa15', null, null, null, null, 'Gi17'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, 'Fa10', 'Fa12', 'Fa14', 'Fa16', null, null, null, null, 'Gi18', null, null, 'Gi19F', 'Gi20F']
		]
	},
	"S1920-18GT2SFP": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, null, null, null, 'Gi17'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, null, null, null, 'Gi18', null, null, 'Gi19F', 'Gi20F']
		]
	},
	"S1920-24GT4SFP/2GT": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S1920-24GT4SFP/2GT-P": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S1920-24T4SFP/2GT": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', null, 'Fa9', 'Fa11', 'Fa13', 'Fa15', null, 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, 'Fa10', 'Fa12', 'Fa14', 'Fa16', null, 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S1920-24T4SFP/2GT-P": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', 'Fa9', 'Fa11', null, 'Fa13', 'Fa15', 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', 'Fa10', 'Fa12', null, 'Fa14', 'Fa16', 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"NBS2028G-E-P": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"OW-NBS2028G-E-P": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"NBS2028G-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"OW-NBS2028G-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"NBS2010G-E-P": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"OW-NBS2010G-E-P": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"NBS2010G-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"OW-NBS2010G-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"NBS2010G-S": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"OW-NBS2010G-S": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"NBS228F-E": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', null, 'Fa9', 'Fa11', 'Fa13', 'Fa15', null, 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, 'Fa10', 'Fa12', 'Fa14', 'Fa16', null, 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"OW-NBS228F-E": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', null, 'Fa9', 'Fa11', 'Fa13', 'Fa15', null, 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, 'Fa10', 'Fa12', 'Fa14', 'Fa16', null, 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"NBS228F-S": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', null, 'Fa9', 'Fa11', 'Fa13', 'Fa15', null, 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, 'Fa10', 'Fa12', 'Fa14', 'Fa16', null, 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"OW-NBS228F-S": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', null, 'Fa9', 'Fa11', 'Fa13', 'Fa15', null, 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, 'Fa10', 'Fa12', 'Fa14', 'Fa16', null, 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"NBS228F-E-LP": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', 'Fa9', 'Fa11', null, 'Fa13', 'Fa15', 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', 'Fa10', 'Fa12', null, 'Fa14', 'Fa16', 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"OW-NBS228F-E-LP": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', 'Fa9', 'Fa11', null, 'Fa13', 'Fa15', 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', 'Fa10', 'Fa12', null, 'Fa14', 'Fa16', 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"NBS228F-E-P": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', 'Fa9', 'Fa11', null, 'Fa13', 'Fa15', 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', 'Fa10', 'Fa12', null, 'Fa14', 'Fa16', 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"OW-NBS228F-E-P": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7', 'Fa9', 'Fa11', null, 'Fa13', 'Fa15', 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Gi25'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', 'Fa10', 'Fa12', null, 'Fa14', 'Fa16', 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"NBS2028G-E-LP": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"OW-NBS2028G-E-LP": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"NBS2028G-S": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"OW-NBS2028G-S": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', null, null, null, null, 'Gi27F', 'Gi28F']
		]
	},
	"NBS210F-S": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"OW-NBS210F-S": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"NBS210F-E-P": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"OW-NBS210F-E-P": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"NBS210F-E": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"OW-NBS210F-E": {
		ports: [
			[null, null, null, null, 'Fa1', 'Fa3', 'Fa5', 'Fa7'],
			[null, null, null, null, 'Fa2', 'Fa4', 'Fa6', 'Fa8', null, null, null, null, 'Gi9', 'Gi10F']
		]
	},
	"S2952G-E V3": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Gi49F', 'Gi51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Gi50F', 'Gi52F'],
		]
	},
	"S2910-48GT4SFP-L": {
		ports: [
			['Fa1', 'Fa3', 'Fa5', 'Fa7', 'Fa9', 'Fa11', null, 'Fa13', 'Fa15', 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Fa25', 'Fa27', 'Fa29', 'Fa31', 'Fa33', 'Fa35', null, 'Fa37', 'Fa39', 'Fa41', 'Fa43', 'Fa45', 'Fa47', null, 'Gi49F', 'Gi51F'],
			['Fa2', 'Fa4', 'Fa6', 'Fa8', 'Fa10', 'Fa12', null, 'Fa14', 'Fa16', 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Fa26', 'Fa28', 'Fa30', 'Fa32', 'Fa34', 'Fa36', null, 'Fa38', 'Fa40', 'Fa42', 'Fa44', 'Fa46', 'Fa48', null, 'Gi50F', 'Gi52F'],
		]
	},
	"NBS5710-48GT4SFP-E": {
		ports: [
			['Fa1', 'Fa3', 'Fa5', 'Fa7', 'Fa9', 'Fa11', null, 'Fa13', 'Fa15', 'Fa17', 'Fa19', 'Fa21', 'Fa23', null, 'Fa25', 'Fa27', 'Fa29', 'Fa31', 'Fa33', 'Fa35', null, 'Fa37', 'Fa39', 'Fa41', 'Fa43', 'Fa45', 'Fa47', null, 'Gi49F', 'Gi51F'],
			['Fa2', 'Fa4', 'Fa6', 'Fa8', 'Fa10', 'Fa12', null, 'Fa14', 'Fa16', 'Fa18', 'Fa20', 'Fa22', 'Fa24', null, 'Fa26', 'Fa28', 'Fa30', 'Fa32', 'Fa34', 'Fa36', null, 'Fa38', 'Fa40', 'Fa42', 'Fa44', 'Fa46', 'Fa48', null, 'Gi50F', 'Gi52F'],
		]
	},
	"S2910-24GT4XS-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S2910-24GT4XS-L": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S5750-24GT4XS-L": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S2910C-24GT2XS-P-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi23F', 'Gi24F', 'Te25F', 'Te26F']
		]
	},
	"S2910C-24GT2XS-HP-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi23F', 'Gi24F', 'Te25F', 'Te26F']
		]
	},
	"S2910-48GT4XS-E": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"S2910-48GT4XS-L": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"S5750-48GT4XS-L": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},

	"S2910C-48GT2XS-HP-E": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Gi47F', 'Te49F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Gi48F', 'Te50F']
		]
	},
	"S2910-24GT4XS-PS-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"M2910-01XS": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1F']
		]
	},
	"M2910-02XS": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1F', null, null, 'Te2F']
		]
	},
	"M2910-04XS": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1F', null, null, 'Te2F', null, null, 'Te3F', null, null, 'Te4F']
		]
	},
	"M2910-01XT": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1']
		]
	},
	"M2910-02XT": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1F', null, null, 'Te2']
		]
	},
	"NBS5628XG": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"NBS5652XG": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"NBS5528XG": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"NBS5552XG": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"S5750-24GT4XS-H": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S5750-48GT4XS-H": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"S5750-48GT4XS-HP-H": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"S5750C-28SFP4XS-H": {
		ports: [
			[null, null, null, null, 'Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', null, 'Gi13F', 'Gi15F', 'Gi17F', 'Gi19F', 'Gi21F', 'Gi23F', null, 'Gi25F', 'Gi27F'],
			[null, null, null, null, 'Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', null, 'Gi14F', 'Gi16F', 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', null, 'Gi26F', 'Gi28F', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"S5750V2-28SFP4XS-L": {
		ports: [
			[null, null, null, null, 'Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', null, 'Gi13F', 'Gi15F', 'Gi17F', 'Gi19F', 'Gi21F', 'Gi23F', null, 'Gi25F', 'Gi27F'],
			[null, null, null, null, 'Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', null, 'Gi14F', 'Gi16F', 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', null, 'Gi26F', 'Gi28F', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"M5000H-04XS": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1F', null, null, 'Te2F', null, null, 'Te3F', null, null, 'Te4F']
		]
	},
	"M5000H-01QXS": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Tw1F']
		]
	},
	"S2910-24GT4SFP-UP-H": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S2910-24GT4SFP-P-L": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"NBS5710-24GT4SFP-E-P": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"DS5730-24GT4SFP-P-L": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S2528G-24P V3": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S2910-24GT4XS-UP-H": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S2910-24GT4XS-P-L": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S2910-10GT2SFP-UP-H": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi9', null, 'Gi10', null, 'Gi11F', null, 'Gi12F']
		]
	},
	"S2910-10GT2SFP-P-E": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi9', null, 'Gi10', null, 'Gi11F', null, 'Gi12F']
		]
	},
	"NBS2009G-P": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi9', null, 'Gi10', null, 'Gi11F', null, 'Gi12F']
		]
	},
	"OW-NBS2009G-P": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi9', null, 'Gi10', null, 'Gi11F', null, 'Gi12F']
		]
	},
	"S2710G-P": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi9', null, 'Gi10', null, 'Gi11F', null, 'Gi12F']
		]
	},
	"DS5730-24GT4XS-S": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S6000C-48GT4XS-E": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"M6000E-04XS": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1F', null, null, 'Te2F', null, null, 'Te3F', null, null, 'Te4F']
		]
	},
	"S2928G-E V3": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S2910-24GT4SFP-L": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"DS5730-24GT4SFP-L": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"NBS5710-24GT4SFP-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},

	"DS5730-48GT4SFP-L": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Gi49F', 'Gi51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Gi50F', 'Gi52F']
		]
	},
	"NBS2026G V2": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi23F', 'Gi24F', 'Gi25F', 'Gi26F']
		]
	},
	"OW-NBS2026G V2": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi23F', 'Gi24F', 'Gi25F', 'Gi26F']
		]
	},
	"S2928G-S V2": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S5750-28GT-S V2": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"S2952G-S V2": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Gi49F', 'Gi51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Gi50F', 'Gi52F']
		]
	},
	"S5750C-28GT4XS-H": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"S5750V2-28GT4XS-L": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"NBS5750-28GT4XS-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"DS5750-28GT4XS-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"S5750C-48GT4XS-H": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"S5310-24GT4XS": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S5310-48GT4XS": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},

	"S5750C-48GT4XS-HP-H": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"M5000H-01XT": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1']
		]
	},
	"M5000H-MSC": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1']
		]
	},

	"M5000H-04XS-A": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1F', null, null, 'Te2F', null, null, 'Te3F', null, null, 'Te4F']
		]
	},
	"M5000H-08XS-A": {
		ports: [
			[null],
			[null, null, null, null, 'Te1F', null, null, 'Te2F', null, null, 'Te3F', null, null, 'Te4F', null, null, 'Te5F', null, null, 'Te6F', null, null, 'Te7F', null, null, 'Te8F']
		]
	},
	"M5000H-08XT-A": {
		ports: [
			[null],
			[null, null, null, null, 'Te1', null, null, 'Te2', null, null, 'Te3', null, null, 'Te4', null, null, 'Te5', null, null, 'Te6', null, null, 'Te7', null, null, 'Te8']
		]
	},
	"S6000C-28GT4XS-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"M6000E-04XS-A": {
		ports: [
			[null],
			[null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, 'Te1F', null, null, 'Te2F', null, null, 'Te3F', null, null, 'Te4F']
		]
	},
	"M6000E-08XS-A": {
		ports: [
			[null],
			[null, null, null, null, 'Te1F', null, null, 'Te2F', null, null, 'Te3F', null, null, 'Te4F', null, null, 'Te5F', null, null, 'Te6F', null, null, 'Te7F', null, null, 'Te8F']
		]
	},
	"M6000E-08XT-A": {
		ports: [
			[null],
			[null, null, null, null, 'Te1', null, null, 'Te2', null, null, 'Te3', null, null, 'Te4', null, null, 'Te5', null, null, 'Te6', null, null, 'Te7', null, null, 'Te8']
		]
	},
	"ES224GT": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"ES324GT": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},

	"S2910-24GT2XS2MG-UP-H": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"AS208GT-P": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi9', null, 'Gi10', null, 'Gi11F', null, 'Gi12F']
		]
	},
	"S5750-48GT/4SFP-P V5.0": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Gi49F', 'Gi51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Gi50F', 'Gi52F']
		]
	},
	"S5750C-48GT2XS-HP": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Gi47F', 'Te49F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Gi48F', 'Te50F']
		]
	},
	"QSW-8350-52T": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Gi47F', 'Te49F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Gi48F', 'Te50F']
		]
	},
	"S5750C-24GT2XS-HP": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi23F', 'Gi24F', 'Te25F', 'Te26F']
		]
	},
	"M8600E-48XS-DB": {
		ports: [
			['Te1F', 'Te3F', 'Te5F', 'Te7F', 'Te9', 'Te11F', 'Te13F', 'Te15F', null, 'Te17F', 'Te19F', 'Te21F', 'Te23F', 'Te25F', 'Te27F', 'Te29F', 'Te31F', null, 'Te33F', 'Te35F', 'Te37F', 'Te39F', 'Te41F', 'Te43F', 'Te45F', 'Te47F'],
			['Te2F', 'Te4F', 'Te6F', 'Te8F', 'Te10F', 'Te12F', 'Te14F', 'Te16F', null, 'Te18F', 'Te20F', 'Te22F', 'Te24F', 'Te26F', 'Te28F', 'Te30F', 'Te32F', null, 'Te34F', 'Te36F', 'Te38F', 'Te40F', 'Te42F', 'Te44F', 'Te46F', 'Te48F']
		]
	},
	"M8600E-48GT-EF": {
		ports: [
			['Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', 'Gi13F', 'Gi15F', null, 'Gi17F', 'Gi19', 'Gi21F', 'Gi23F', 'Gi25F', 'Gi27F', 'Gi29F', 'Gi31F', null, 'Gi33F', 'Gi35F', 'Gi37', 'Gi39F', 'Gi41F', 'Gi43F', 'Gi45F', 'Gi47F'],
			['Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', 'Gi14F', 'Gi16F', null, 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', 'Gi26F', 'Gi28F', 'Gi30F', 'Gi32F', null, 'Gi34F', 'Gi36F', 'Gi38F', 'Gi40F', 'Gi42F', 'Gi44F', 'Gi46F', 'Gi48F']
		]
	},
	"M18000-48SFP-DB": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48']
		]
	},
	"M7800E-48XS-DB": {
		ports: [
			['Te1F', 'Te3F', 'Te5F', 'Te7F', 'Te9', 'Te11F', 'Te13F', 'Te15F', null, 'Te17F', 'Te19F', 'Te21F', 'Te23F', 'Te25F', 'Te27F', 'Te29F', 'Te31F', null, 'Te33F', 'Te35F', 'Te37F', 'Te39F', 'Te41F', 'Te43F', 'Te45F', 'Te47F'],
			['Te2F', 'Te4F', 'Te6F', 'Te8F', 'Te10F', 'Te12F', 'Te14F', 'Te16F', null, 'Te18F', 'Te20F', 'Te22F', 'Te24F', 'Te26F', 'Te28F', 'Te30F', 'Te32F', null, 'Te34F', 'Te36F', 'Te38F', 'Te40F', 'Te42F', 'Te44F', 'Te46F', 'Te48F']
		]
	},
	"QSW-8350-32T": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"QSW-8350-32F": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"S5750C-28GT4XS-HP-H": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"S2910-10GT2SFP-UP-E": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi9', null, 'Gi10', null, 'Gi11F', null, 'Gi12F']
		]
	},
	"FH-S2910C-24GT2XS-HP-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Gi23F', 'Gi24F', 'Te25F', 'Te26F']
		]
	},
	"FH-S5750C-28GT4XS-H": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', null, null, 'Te29F', 'Te30F', 'Te31F', 'Te32F']
		]
	},
	"S2930-12GT12MG4XS-UP-H": {
		ports: [
			[null, null, null, null, 'MT1', 'MT3', 'MT5', 'MT7', 'MT9', 'MT11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'MT2', 'MT4', 'MT6', 'MT8', 'MT10', 'MT12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"M7500-48GT4XS-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"M7800C-48GT4XS-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"M7500-36GT12SFP4XS-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37F', 'Gi39F', 'Gi41F', 'Gi43F', 'Gi45F', 'Gi47F', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38F', 'Gi40F', 'Gi42F', 'Gi44F', 'Gi46F', 'Gi48F', null, 'Te50F', 'Te52F']
		]
	},
	"M7800C-36GT12SFP4XS-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37F', 'Gi39F', 'Gi41F', 'Gi43F', 'Gi45F', 'Gi47F', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38F', 'Gi40F', 'Gi42F', 'Gi44F', 'Gi46F', 'Gi48F', null, 'Te50F', 'Te52F']
		]
	},
	"M7500-24SFP/12GT4XS-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', null, 'Gi13F', 'Gi15F', 'Gi17F', 'Gi19F', 'Gi21F', 'Gi23F', null, 'Te25F', 'Te27F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', null, 'Gi14F', 'Gi16F', 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', null, 'Te26F', 'Te28F']
		]
	},
	"M7800C-24SFP/12GT4XS-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', null, 'Gi13F', 'Gi15F', 'Gi17F', 'Gi19F', 'Gi21F', 'Gi23F', null, 'Te25F', 'Te27F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', null, 'Gi14F', 'Gi16F', 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', null, 'Te26F', 'Te28F']
		]
	},
	"M7500-48SFP4XS-EA": {
		ports: [
			['Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', null, 'Gi13F', 'Gi15F', 'Gi17F', 'Gi19F', 'Gi21F', 'Gi23F', null, 'Gi25F', 'Gi27F', 'Gi29F', 'Gi31F', 'Gi33F', 'Gi35F', null, 'Gi37F', 'Gi39F', 'Gi41F', 'Gi43F', 'Gi45F', 'Gi47F', null, 'Te49F', 'Te51F'],
			['Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', null, 'Gi14F', 'Gi16F', 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', null, 'Gi26F', 'Gi28F', 'Gi30F', 'Gi32F', 'Gi34F', 'Gi36F', null, 'Gi38F', 'Gi40F', 'Gi42F', 'Gi44F', 'Gi46F', 'Gi48F', null, 'Te50F', 'Te52F']
		]
	},
	"M7800C-48SFP4XS-EA": {
		ports: [
			['Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', null, 'Gi13F', 'Gi15F', 'Gi17F', 'Gi19F', 'Gi21F', 'Gi23F', null, 'Gi25F', 'Gi27F', 'Gi29F', 'Gi31F', 'Gi33F', 'Gi35F', null, 'Gi37F', 'Gi39F', 'Gi41F', 'Gi43F', 'Gi45F', 'Gi47F', null, 'Te49F', 'Te51F'],
			['Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', null, 'Gi14F', 'Gi16F', 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', null, 'Gi26F', 'Gi28F', 'Gi30F', 'Gi32F', 'Gi34F', 'Gi36F', null, 'Gi38F', 'Gi40F', 'Gi42F', 'Gi44F', 'Gi46F', 'Gi48F', null, 'Te50F', 'Te52F']
		]
	},
	"M7500-24GT24SFP4XS-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25F', 'Gi27F', 'Gi29F', 'Gi31F', 'Gi33F', 'Gi35F', null, 'Gi37F', 'Gi39F', 'Gi41F', 'Gi43F', 'Gi45F', 'Gi47F', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26F', 'Gi28F', 'Gi30F', 'Gi32F', 'Gi34F', 'Gi36F', null, 'Gi38F', 'Gi40F', 'Gi42F', 'Gi44F', 'Gi46F', 'Gi48F', null, 'Te50F', 'Te52F']
		]
	},
	"M7800C-24GT24SFP4XS-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25F', 'Gi27F', 'Gi29F', 'Gi31F', 'Gi33F', 'Gi35F', null, 'Gi37F', 'Gi39F', 'Gi41F', 'Gi43F', 'Gi45F', 'Gi47F', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26F', 'Gi28F', 'Gi30F', 'Gi32F', 'Gi34F', 'Gi36F', null, 'Gi38F', 'Gi40F', 'Gi42F', 'Gi44F', 'Gi46F', 'Gi48F', null, 'Te50F', 'Te52F']
		]
	},
	"NBS5710-48GT2XS-E-P": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Gi47F', 'Te49F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Gi48F', 'Te50F']
		]
	},
	"M7800C-16SFP8XS-EA": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, null, null, null, 'Gi13', 'Gi15', 'Te17F', 'Te19F', 'Te21F', 'Te23F'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, null, null, null, 'Gi14', 'Gi16', 'Te18F', 'Te20F', 'Te22F', 'Te24F']
		]
	},
	"M7800C-48GT4XS-P-EA": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Gi25', 'Gi27', 'Gi29', 'Gi31', 'Gi33', 'Gi35', null, 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi26', 'Gi28', 'Gi30', 'Gi32', 'Gi34', 'Gi36', null, 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"ES226GC-P": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi25F', 'Gi26F']
		]
	},
	"ES205C-P": {
		ports: [
			['Fa1', 'Fa2', 'Fa3', 'Fa4', null, 'Fa5'],
		]
	},
	"ES205GC-P": {
		ports: [
			['Gi1', 'Gi2', 'Gi3', 'Gi4', null, 'Gi5'],
		]
	},
	"ES209C-P": {
		ports: [
			['Fa1', 'Fa2', 'Fa3', 'Fa4', 'Fa5', 'Fa6', 'Fa7', 'Fa8', null, 'Fa9'],
		]
	},
	"ES209GC-P": {
		ports: [
			['Gi1', 'Gi2', 'Gi3', 'Gi4', 'Gi5', 'Gi6', 'Gi7', 'Gi8', null, 'Gi9'],
		]
	},
	"ES218GC-P": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, null, null, null, 'Gi17F', 'Gi18F'],
		]
	},
	"S2910-12GT2SFP-E": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, null, null, null, 'Gi13F', null, null, null, null, 'Gi14F'],
		]
	},
	"S5750V2-48GT4XS-L": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"S5750-24GT4XS-HP-H": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', null, 'Te25F', 'Te27F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Te26F', 'Te28F']
		]
	},
	"NBS5720-24GT4XS-S": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"NBS5720-24GT4XS-E": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"NBS5720-24SFP/8GT4XS": {
		ports: [
			[null, null, null, null, 'Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', null, 'Gi13F', 'Gi15F', 'Gi17F', 'Gi19F', 'Gi21F', 'Gi23F', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', null, 'Gi14F', 'Gi16F', 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"NBS5720-48GT4XS-E": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"NBS5720-48GT4XS-P": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"NBS5720-24GT4XS": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"NBS5720-24GT4XS-P": {
		ports: [
			[null, null, null, null, 'Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', null, 'Gi13', 'Gi15', 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			[null, null, null, null, 'Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', null, 'Gi14', 'Gi16', 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, null, 'Te25F', 'Te26F', 'Te27F', 'Te28F']
		]
	},
	"NBS5720-48GT4XS-S": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', 'Gi25', 'Gi27', 'Gi29', 'Gi31', null, 'Gi33', 'Gi35', 'Gi37', 'Gi39', 'Gi41', 'Gi43', 'Gi45', 'Gi47', null, 'Te49F', 'Te51F'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', 'Gi26', 'Gi28', 'Gi30', 'Gi32', null, 'Gi34', 'Gi36', 'Gi38', 'Gi40', 'Gi42', 'Gi44', 'Gi46', 'Gi48', null, 'Te50F', 'Te52F']
		]
	},
	"S5750C-48SFP4XS-H": {
		ports: [
			['Gi1F', 'Gi3F', 'Gi5F', 'Gi7F', 'Gi9F', 'Gi11F', 'Gi13F', 'Gi15F', null, 'Gi17F', 'Gi19F', 'Gi21F', 'Gi23F', 'Gi25F', 'Gi27F', 'Gi29F', 'Gi31F', null, 'Gi33F', 'Gi35F', 'Gi37F', 'Gi39F', 'Gi41F', 'Gi43F', 'Gi45F', 'Gi47F', null, 'Te49F', 'Te51F'],
			['Gi2F', 'Gi4F', 'Gi6F', 'Gi8F', 'Gi10F', 'Gi12F', 'Gi14F', 'Gi16F', null, 'Gi18F', 'Gi20F', 'Gi22F', 'Gi24F', 'Gi26F', 'Gi28F', 'Gi30F', 'Gi32F', null, 'Gi34F', 'Gi36F', 'Gi38F', 'Gi40F', 'Gi42F', 'Gi44F', 'Gi46F', 'Gi48F', null, 'Te50F', 'Te52F']
		]
	},
	"NBS2116GT2SFP": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null,],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi17F', 'Gi18F']
		]
	},
	"NBS2124GT2SFP": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', null,],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi25F', 'Gi26F']
		]
	},
	"NBS2100-16GT2SFP": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null,],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi17F', 'Gi18F']
		]
	},
	"NBS2100-24GT2SFP": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23', null,],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi25F', 'Gi26F']
		]
	},
	"ES216GC": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16']
		]
	},
	"ES224GC": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24']
		]
	},
	"NBS3100-24GT4SFP": {
		ports: [
			['Gi1', 'Gi3', 'Gi5', 'Gi7', null, 'Gi9', 'Gi11', 'Gi13', 'Gi15', null, 'Gi17', 'Gi19', 'Gi21', 'Gi23'],
			['Gi2', 'Gi4', 'Gi6', 'Gi8', null, 'Gi10', 'Gi12', 'Gi14', 'Gi16', null, 'Gi18', 'Gi20', 'Gi22', 'Gi24', null, 'Gi25F', 'Gi26F', 'Gi27F', 'Gi28F']
		]
	},
	"NBS3100-24GT4SFP-P": {
		ports: [
			["Gi1", "Gi3", "Gi5", "Gi7", null, "Gi9", "Gi11", "Gi13", "Gi15", null, "Gi17", "Gi19", "Gi21", "Gi23"],
			["Gi2", "Gi4", "Gi6", "Gi8", null, "Gi10", "Gi12", "Gi14", "Gi16", null, "Gi18", "Gi20", "Gi22", "Gi24", null, "Gi25F", "Gi26F", "Gi27F", "Gi28F"]
		]
	},
	"NBS3200-24SFP/8GT4XS": {
		ports: [
			["Gi1F", "Gi3F", "Gi5F", "Gi7F", "Gi9F", "Gi11F", null, "Gi13F", "Gi15F", "Gi17F", "Gi19F", "Gi21F", "Gi23F", null, "Gi17", "Gi19", "Gi21", "Gi23"],
			["Gi2F", "Gi4F", "Gi6F", "Gi8F", "Gi10F", "Gi12F", null, "Gi14F", "Gi16F", "Gi18F", "Gi20F", "Gi22F", "Gi24F", null, "Gi18", "Gi20", "Gi22", "Gi24", null, "Te25F", "Te26F", "Te27F", "Te28F"]
		]
	},
	"NBS3200-48GT4XS": {
		ports: [
			["Gi1", "Gi3", "Gi5", "Gi7", "Gi9", "Gi11", null, "Gi13", "Gi15", "Gi17", "Gi19", "Gi21", "Gi23", null, "Gi25", "Gi27", "Gi29", "Gi31", "Gi33", "Gi35", null, "Gi37", "Gi39", "Gi41", "Gi43", "Gi45", "Gi47", null, "Te49F", "Te51F"],
			["Gi2", "Gi4", "Gi6", "Gi8", "Gi10", "Gi12", null, "Gi14", "Gi16", "Gi18", "Gi20", "Gi22", "Gi24", null, "Gi26", "Gi28", "Gi30", "Gi32", "Gi34", "Gi36", null, "Gi38", "Gi40", "Gi42", "Gi44", "Gi46", "Gi48", null, "Te50F", "Te52F"]
		]
	},
	"NBS3200-48GT4XS-P": {
		ports: [
			["Gi1", "Gi3", "Gi5", "Gi7", "Gi9", "Gi11", null, "Gi13", "Gi15", "Gi17", "Gi19", "Gi21", "Gi23", null, "Gi25", "Gi27", "Gi29", "Gi31", "Gi33", "Gi35", null, "Gi37", "Gi39", "Gi41", "Gi43", "Gi45", "Gi47", null, "Te49F", "Te51F"],
			["Gi2", "Gi4", "Gi6", "Gi8", "Gi10", "Gi12", null, "Gi14", "Gi16", "Gi18", "Gi20", "Gi22", "Gi24", null, "Gi26", "Gi28", "Gi30", "Gi32", "Gi34", "Gi36", null, "Gi38", "Gi40", "Gi42", "Gi44", "Gi46", "Gi48", null, "Te50F", "Te52F"]
		]
	},
	"NBS3200-24GT4XS": {
		ports: [
			["Gi1", "Gi3", "Gi5", "Gi7", "Gi9", "Gi11", null, "Gi13", "Gi15", "Gi17", "Gi19", "Gi21", "Gi23"],
			["Gi2", "Gi4", "Gi6", "Gi8", "Gi10", "Gi12", null, "Gi14", "Gi16", "Gi18", "Gi20", "Gi22", "Gi24", null, "Te25F", "Te26F", "Te27F", "Te28F"]
		]
	},
	"NBS3200-24GT4XS-P": {
		ports: [
			["Gi1", "Gi3", "Gi5", "Gi7", "Gi9", "Gi11", null, "Gi13", "Gi15", "Gi17", "Gi19", "Gi21", "Gi23"],
			["Gi2", "Gi4", "Gi6", "Gi8", "Gi10", "Gi12", null, "Gi14", "Gi16", "Gi18", "Gi20", "Gi22", "Gi24", null, "Te25F", "Te26F", "Te27F", "Te28F"]
		]
	},
	"NBS3100-8GT2SFP-P": {
		ports: [
			["Gi1", "Gi3", "Gi5", "Gi7"],
			["Gi2", "Gi4", "Gi6", "Gi8", null, "Gi9F", "Gi10F"]
		]
	},
	"NBS3100-8GT2SFP": {
		ports: [
			["Gi1", "Gi3", "Gi5", "Gi7"],
			["Gi2", "Gi4", "Gi6", "Gi8", null, "Gi9F", "Gi10F"]
		]
	}
};

export default SwitchTypeList