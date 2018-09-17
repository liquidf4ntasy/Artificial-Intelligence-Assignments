function [] = main()
    obj = Decision_tree();
    %obj.class_info_gain(obj);
    obj.attr_no = zeros(size(obj.train_data, 2)-1, 1);
    tree = [];
    for i = 1: size(obj.train_data, 2)-1
        obj.attr_no(i, 1) = i;
    end
    
    maketree(obj.train_data, 1, tree, 1, 0, obj);
     
    
end