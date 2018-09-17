function [tree] = maketree(data, pruning_thr, tree, index, rc, obj)
    rc = rc+1;
    target = data(:, end);
    if size(data, 1) < 50
        disp('terminating')
        distribution = histc(target(:), unique(target));
        distribution = distribution / size(target, 1);
        [m , i] = max(distribution);
        tree(index) = i;
    elseif size(unique(target)) == 0
        disp('all classes are same')
        tree(index) = unique(target(1, 1));
    else
    [best_attr, best_thr, best_gain, right_data, left_data] = obj.choose_attribute(data, obj);
    fprintf('best_attr = %f, best_thr = %f, best_gain = %f \n', best_attr, best_thr, best_gain);
    tree(index) = best_attr;
    
    tree = maketree(left_data, pruning_thr, tree, index*2, rc+1, obj);
    tree = maketree(right_data, pruning_thr, tree, (index*2)+1, rc, obj);
    end
    
    

end