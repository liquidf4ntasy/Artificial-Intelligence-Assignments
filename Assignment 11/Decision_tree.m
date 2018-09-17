classdef Decision_tree < handle
    properties
    train_file = 'pendigits_training.txt';
    test_file = 'pendigits_test.txt';
    train_data;
    target;
    unique_class;
    count_class;
    class_probab;
    class_gain;
    threshold = 70;
    info_gain_1;
    info_gain_2;
    value_1;
    value_2;
    value_1_probab;
    value_2_probab;
    value_1_classes;
    value_2_classes;
    ig_1;
    ig_2;
    entropy;
    gain;
    max_gain;
    bst_attribute;
    bst_thr;
    attr_no;
    right_data;
    left_data;
    end    
    methods (Static)
        function [obj] = Decision_tree()
            obj.train_data = load(obj.train_file);
            obj.target = obj.train_data(1:end, end);
            %obj.train_data = obj.train_data(1:end, 1:end-1);
            obj.unique_class = unique(obj.target);
            obj.count_class = histc(obj.target, obj.unique_class);
            obj.class_probab = obj.count_class ./ size(obj.target, 1);
            obj.gain = zeros(size(obj.train_data, 2)-1, 50);
            obj.threshold = zeros(16, 50);
        end
        
        function [obj] = class_info_gain(obj)
            obj.class_gain = 0;
            
            for i = 1:size(obj.class_probab, 1)
                obj.class_gain = obj.class_gain + (obj.class_probab(i, 1) * (log(obj.class_probab(i, 1))/log(2)));
            end
            
            obj.class_gain = (-1)*obj.class_gain;
        end
        
        function [bst_attribute, bst_thr, max_gain, right_data, left_data] = choose_attribute(data, obj)
            obj.target = data(1:end, end);
            obj.unique_class = unique(obj.target);
            obj.count_class = histc(obj.target, obj.unique_class);
            obj.class_probab = obj.count_class ./ size(obj.target, 1);
            
            obj.class_gain = 0;
            
            for i = 1:size(obj.class_probab, 1)
                obj.class_gain = obj.class_gain + (obj.class_probab(i, 1) * (log(obj.class_probab(i, 1))/log(2)));
            end
            
            obj.class_gain = (-1)*obj.class_gain;
            
            obj.max_gain = -1; 
            
            for attribute = 1: size(data, 2)-1
                
                L = min(data(:, attribute));
                M = max(data(:, attribute));
                
                for K = 1:50
                    obj.threshold(attribute, K) = L + K * (M - L) /51;
                    thr = obj.threshold(attribute, K);
                    
                    obj.info_gain_1 = zeros(size(obj.unique_class));
                    obj.info_gain_2 = zeros(size(obj.unique_class));

                    find_1 = data(1:end, attribute) < thr;
                    find_2 = data(1:end, attribute) >= thr;

                    obj.value_1 = data(find_1, :);
                    obj.value_2 = data(find_2, :);

                    obj.value_1_classes = histc(obj.value_1(:, end), obj.unique_class);
                    obj.value_2_classes = histc(obj.value_2(:, end), obj.unique_class);

                    if isrow(obj.value_1_classes)
                        obj.value_1_classes = transpose(obj.value_1_classes);
                    end

                    if isrow(obj.value_2_classes)
                        obj.value_2_classes = transpose(obj.value_2_classes);
                    end

                    obj.value_1_probab = obj.value_1_classes / sum(obj.value_1_classes);
                    obj.value_1_probab(isnan(obj.value_1_probab)) = 0;

                    obj.value_2_probab = obj.value_2_classes / sum(obj.value_2_classes);
                    obj.value_2_probab(isnan(obj.value_2_probab)) = 0;

                    for class = 1: size(obj.unique_class)
                        obj.info_gain_1(class, 1) = obj.value_1_probab(class, 1)*(log2(obj.value_1_probab(class, 1)));
                    end

                    obj.info_gain_1(isnan(obj.info_gain_1)) = 0;
                    obj.info_gain_1 = (-1)*obj.info_gain_1;
                    obj.ig_1 = sum(obj.info_gain_1);

                    for class = 1: size(obj.unique_class)
                        obj.info_gain_2(class, 1) = obj.value_2_probab(class, 1)*(log2(obj.value_2_probab(class, 1)));
                    end
                    
                    obj.info_gain_2(isnan(obj.info_gain_2)) = 0;
                    obj.info_gain_2 = (-1)*obj.info_gain_2;
                    obj.ig_2 = sum(obj.info_gain_2);

                    obj.entropy = (sum(obj.value_1_classes)/size(obj.train_data, 1))*obj.ig_1 + (sum(obj.value_2_classes)/size(obj.train_data, 1))*obj.ig_2;

                    obj.gain(attribute, K) = obj.class_gain - obj.entropy;
                    
                    if obj.gain(attribute, K) > obj.max_gain 
                        
                        obj.max_gain = obj.gain(attribute, K);
                        obj.bst_thr = obj.threshold(attribute, K);
                        obj.bst_attribute = attribute;
                        obj.right_data = obj.value_1;
                        obj.left_data = obj.value_2;
                        
                        max_gain = obj.max_gain;
                        bst_thr = obj.bst_thr;
                        bst_attribute = obj.bst_attribute;
                        right_data = obj.right_data;
                        left_data = obj.left_data;
                        
                    end
                end
            end
        end
        
    end
end