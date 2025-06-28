/**
 * 类别
 */
import {API_PROP} from '@/services/api';
import BaseService from '@/services/baseService';


let Service = Object.assign({}, BaseService,{ urlPrefix: API_PROP});

export default Service;